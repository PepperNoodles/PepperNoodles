/**
 * Mirrors the backend DTOs. Kept hand-written and small rather than generated,
 * so the shapes the UI depends on are visible in one place.
 */

export type Role = "ROLE_USER" | "ROLE_COMPANY" | "ROLE_ADMIN";

export interface Page<T> {
  content: T[];
  page: number;
  size: number;
  totalElements: number;
  totalPages: number;
  first: boolean;
  last: boolean;
}

export interface AuthUser {
  id: number;
  email: string;
  displayName: string;
  avatarPath?: string | null;
  roles: Role[];
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  expiresIn: number;
  expiresAt: string;
  user: AuthUser;
}

export interface Tag {
  id: number;
  name: string;
}

export interface Rating {
  average: string | null;
  ratingCount: number;
  reviewCount: number;
}

export interface RestaurantSummary {
  id: number;
  name: string;
  address: string;
  contact?: string | null;
  website?: string | null;
  photoUrl?: string | null;
  latitude: string;
  longitude: string;
  rating: Rating;
  tags: Tag[];
  distanceMetres?: number | null;
}

export interface BusinessHour {
  id?: number;
  dayOfWeek: number;
  opensAt: string;
  closesAt: string;
}

export interface RestaurantEvent {
  id: number;
  restaurantId: number;
  name: string;
  content?: string | null;
  imageUrl?: string | null;
  startsOn: string;
  endsOn: string;
  active: boolean;
}

export interface RestaurantDetail extends Omit<RestaurantSummary, "distanceMetres"> {
  businessHours: BusinessHour[];
  menu: { id: number; caption?: string | null; imageUrl: string; position: number }[];
  activeEvents: RestaurantEvent[];
  owner: { userId: number; name: string };
  favourited: boolean;
  editable: boolean;
  createdAt: string;
}

export interface ReviewAuthor {
  userId: number;
  displayName: string;
  avatarUrl?: string | null;
}

export interface Review {
  id: number;
  author: ReviewAuthor;
  body: string;
  score?: number | null;
  createdAt: string;
  editable: boolean;
  replies: {
    id: number;
    author: ReviewAuthor;
    body: string;
    createdAt: string;
    fromRestaurantOwner: boolean;
    editable: boolean;
  }[];
}

export interface Product {
  id: number;
  name: string;
  price: string;
  quantity: number;
  imageUrl?: string | null;
  status: "LISTED" | "DELISTED";
  restaurantId: number;
  restaurantName: string;
  categoryName?: string | null;
  subcategoryName?: string | null;
  tags: Tag[];
}

export interface ProductDetail extends Product {
  description?: string | null;
  editable: boolean;
  releasedAt?: string | null;
}

export interface Category {
  id: number;
  name: string;
  subcategories: { id: number; name: string }[];
}

export interface CartLine {
  productId: number;
  name: string;
  imageUrl?: string | null;
  unitPrice: string;
  quantity: number;
  lineTotal: string;
  availableStock: number;
  unavailable: boolean;
}

export interface Cart {
  items: CartLine[];
  total: string;
  hasUnavailableItems: boolean;
}

export interface Order {
  id: number;
  orderNo: string;
  status: "PENDING" | "PAID" | "CANCELLED" | "EXPIRED";
  totalCost: string;
  receiverName: string;
  receiverPhone: string;
  receiverAddress: string;
  createdAt: string;
  paidAt?: string | null;
  expiresAt: string;
  items: {
    productId: number | null;
    productName: string;
    unitPrice: string;
    quantity: number;
    lineTotal: string;
  }[];
}

export interface UserProfile {
  userId: number;
  email: string;
  realName?: string | null;
  nickname?: string | null;
  phone?: string | null;
  birthDate?: string | null;
  gender?: "MALE" | "FEMALE" | "OTHER" | null;
  location?: string | null;
  avatarUrl?: string | null;
  foodTags: Tag[];
  roles: Role[];
  stats?: {
    tier: string;
    postCount: number;
    likeCount: number;
    followerCount: number;
    replyCount: number;
    loginCount: number;
    purchaseCount: number;
  } | null;
  createdAt: string;
}

export interface Friend {
  friendshipId: number;
  userId: number;
  nickname: string;
  avatarUrl?: string | null;
  status: "PENDING" | "ACCEPTED" | "DECLINED" | "BLOCKED";
  incoming: boolean;
  since: string;
}

export interface Conversation {
  partnerId: number;
  partnerName: string;
  partnerAvatarUrl?: string | null;
  lastMessage: string;
  lastMessageAt: string;
  unreadCount: number;
}

export interface ChatMessage {
  id: number;
  senderId: number;
  recipientId: number;
  body: string;
  createdAt: string;
  readAt?: string | null;
}

export interface AdminDashboard {
  totalUsers: number;
  suspendedUsers: number;
  totalRestaurants: number;
  totalProducts: number;
  pendingOrders: number;
  openInquiries: number;
}

export interface ManagedUser {
  id: number;
  email: string;
  displayName: string;
  roles: Role[];
  enabled: boolean;
  suspended: boolean;
  suspendedReason?: string | null;
  lastLoginAt?: string | null;
  createdAt: string;
}

export interface Inquiry {
  id: number;
  userId?: number | null;
  submitterName?: string | null;
  contactEmail?: string | null;
  body: string;
  status: "OPEN" | "RESOLVED";
  resolutionNote?: string | null;
  createdAt: string;
  resolvedAt?: string | null;
}

/** RFC 7807 problem detail, as produced by GlobalExceptionHandler. */
export interface ProblemDetail {
  type: string;
  title: string;
  status: number;
  detail: string;
  instance: string;
  code: string;
  timestamp: string;
  errors?: Record<string, string>;
}

/** 首頁 rollups — see the backend's DiscoveryController. */
export interface District {
  district: string;
  restaurantCount: number;
}

export interface Campaign {
  id: number;
  name: string;
  content?: string | null;
  imageUrl?: string | null;
  startsOn: string;
  endsOn: string;
  restaurantId: number;
  restaurantName: string;
}

export interface HighlightReview {
  id: number;
  body: string;
  score?: number | null;
  createdAt: string;
  restaurantId: number;
  restaurantName: string;
  authorName: string;
  authorAvatarUrl?: string | null;
}

/** 論壇 */
export interface ForumAuthor {
  userId: number;
  displayName: string;
  avatarUrl?: string | null;
}

export interface ForumPostSummary {
  id: number;
  author: ForumAuthor;
  excerpt: string;
  imageUrl?: string | null;
  tags: Tag[];
  commentCount: number;
  bookmarkCount: number;
  bookmarked: boolean;
  editable: boolean;
  createdAt: string;
}

export interface ForumReply {
  id: number;
  author: ForumAuthor;
  replyTo?: ForumAuthor | null;
  body: string;
  editable: boolean;
  createdAt: string;
}

export interface ForumComment {
  id: number;
  author: ForumAuthor;
  body: string;
  score?: number | null;
  editable: boolean;
  createdAt: string;
  replies: ForumReply[];
}

export interface ForumPostDetail {
  id: number;
  author: ForumAuthor;
  body: string;
  imageUrl?: string | null;
  tags: Tag[];
  bookmarkCount: number;
  bookmarked: boolean;
  editable: boolean;
  comments: ForumComment[];
  createdAt: string;
  updatedAt: string;
}

/** 留言牆 & 追蹤 */
export interface WallMessage {
  id: number;
  author: ForumAuthor;
  body: string;
  likeCount: number;
  likedByMe: boolean;
  deletable: boolean;
  createdAt: string;
  replies: WallMessage[];
}

export interface FollowUser {
  userId: number;
  displayName: string;
  avatarUrl?: string | null;
  since: string;
}

export interface FollowCounts {
  followers: number;
  following: number;
  followedByMe: boolean;
}

/** Another member's profile as returned by GET /users/{id}. */
export interface PublicProfile {
  userId: number;
  nickname: string;
  avatarUrl?: string | null;
  location?: string | null;
  foodTags: Tag[];
  /** NONE / PENDING / ACCEPTED / DECLINED / BLOCKED, or null when unauthenticated. */
  friendshipStatus?: string | null;
}
