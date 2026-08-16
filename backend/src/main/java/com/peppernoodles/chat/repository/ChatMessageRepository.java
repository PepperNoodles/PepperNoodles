package com.peppernoodles.chat.repository;

import com.peppernoodles.chat.domain.ChatMessage;
import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    /** The thread between two members, newest first. */
    @EntityGraph(attributePaths = {"sender", "recipient"})
    @Query("""
            select m from ChatMessage m
             where (m.sender.id = :a and m.recipient.id = :b)
                or (m.sender.id = :b and m.recipient.id = :a)
             order by m.createdAt desc
            """)
    Page<ChatMessage> findConversation(@Param("a") Long userA, @Param("b") Long userB, Pageable pageable);

    @Modifying
    @Query("""
            update ChatMessage m set m.readAt = :now
             where m.recipient.id = :userId and m.sender.id = :otherId and m.readAt is null
            """)
    int markConversationRead(
            @Param("userId") Long userId, @Param("otherId") Long otherId, @Param("now") Instant now);

    long countByRecipientIdAndReadAtIsNull(Long recipientId);

    /** Distinct counterparts with the most recent message, for the conversation list. */
    @Query(value = """
            select distinct on (partner_id)
                   partner_id as partnerId, body as lastMessage, created_at as lastMessageAt,
                   unread as unreadCount
              from (
                select case when sender_user_id = :userId then recipient_user_id else sender_user_id end as partner_id,
                       body, created_at,
                       (select count(*) from chat_messages u
                         where u.recipient_user_id = :userId
                           and u.sender_user_id = case when m.sender_user_id = :userId
                                                       then m.recipient_user_id else m.sender_user_id end
                           and u.read_at is null) as unread
                  from chat_messages m
                 where m.sender_user_id = :userId or m.recipient_user_id = :userId
              ) t
             order by partner_id, created_at desc
            """, nativeQuery = true)
    List<ConversationRow> findConversations(@Param("userId") Long userId);

    interface ConversationRow {
        Long getPartnerId();

        String getLastMessage();

        Instant getLastMessageAt();

        Long getUnreadCount();
    }
}
