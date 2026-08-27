/**
 * Inline icon set.
 *
 * <p>The first build used emoji — 🍜 for a missing photo, 🎉 on every campaign,
 * ★/☆/✕/✓ as text glyphs. Emoji render differently on every platform, cannot be
 * recoloured, and are read aloud by screen readers as their CLDR name ("pot of
 * food"), so they were never icons in any useful sense.
 *
 * <p>These are 24×24 stroked paths on Lucide's geometry: `currentColor`, 1.75
 * stroke, so an icon inherits the colour and size of the text beside it. They
 * are decorative by default (`aria-hidden`) — pass a `title` only when the icon
 * is the sole content of a control and nothing else names it.
 */
import type { SVGProps } from "react";

type IconProps = SVGProps<SVGSVGElement> & { title?: string };

function Icon({ title, children, ...props }: IconProps & { children: React.ReactNode }) {
  return (
    <svg
      viewBox="0 0 24 24"
      fill="none"
      stroke="currentColor"
      strokeWidth={1.75}
      strokeLinecap="round"
      strokeLinejoin="round"
      width="1em"
      height="1em"
      // An icon with a title is exposed as an image; without one it is noise.
      role={title ? "img" : undefined}
      aria-hidden={title ? undefined : true}
      focusable="false"
      {...props}
    >
      {title && <title>{title}</title>}
      {children}
    </svg>
  );
}

export const IconSearch = (p: IconProps) => (
  <Icon {...p}><circle cx="11" cy="11" r="7" /><path d="m20 20-3.5-3.5" /></Icon>
);

export const IconMapPin = (p: IconProps) => (
  <Icon {...p}><path d="M20 10c0 5.5-8 12-8 12s-8-6.5-8-12a8 8 0 0 1 16 0Z" /><circle cx="12" cy="10" r="3" /></Icon>
);

export const IconClock = (p: IconProps) => (
  <Icon {...p}><circle cx="12" cy="12" r="9" /><path d="M12 7v5l3 2" /></Icon>
);

export const IconPhone = (p: IconProps) => (
  <Icon {...p}><path d="M6.5 3h3l1.5 4-2 1.5a12 12 0 0 0 5.5 5.5L16 12l4 1.5v3a2 2 0 0 1-2.2 2A16.5 16.5 0 0 1 4 6.2 2 2 0 0 1 6 4Z" /></Icon>
);

export const IconGlobe = (p: IconProps) => (
  <Icon {...p}><circle cx="12" cy="12" r="9" /><path d="M3 12h18M12 3a15 15 0 0 1 0 18a15 15 0 0 1 0-18Z" /></Icon>
);

export const IconTag = (p: IconProps) => (
  <Icon {...p}><path d="M3 11.5V4a1 1 0 0 1 1-1h7.5a1 1 0 0 1 .7.3l8.5 8.5a1 1 0 0 1 0 1.4l-7.5 7.5a1 1 0 0 1-1.4 0L3.3 12.2a1 1 0 0 1-.3-.7Z" /><circle cx="7.5" cy="7.5" r="1.25" fill="currentColor" stroke="none" /></Icon>
);

/** Outline star — the empty half of a rating. */
export const IconStar = (p: IconProps) => (
  <Icon {...p}><path d="m12 3.5 2.6 5.4 5.9.8-4.3 4.2 1 5.9-5.2-2.8-5.2 2.8 1-5.9L3.5 9.7l5.9-.8Z" /></Icon>
);

/** Filled star — the earned half of a rating. */
export const IconStarFilled = (p: IconProps) => (
  <Icon {...p} fill="currentColor"><path d="m12 3.5 2.6 5.4 5.9.8-4.3 4.2 1 5.9-5.2-2.8-5.2 2.8 1-5.9L3.5 9.7l5.9-.8Z" /></Icon>
);

export const IconHeart = (p: IconProps) => (
  <Icon {...p}><path d="M12 20s-7.5-4.7-7.5-9.8A4.2 4.2 0 0 1 12 7.4a4.2 4.2 0 0 1 7.5 2.8C19.5 15.3 12 20 12 20Z" /></Icon>
);

export const IconHeartFilled = (p: IconProps) => (
  <Icon {...p} fill="currentColor"><path d="M12 20s-7.5-4.7-7.5-9.8A4.2 4.2 0 0 1 12 7.4a4.2 4.2 0 0 1 7.5 2.8C19.5 15.3 12 20 12 20Z" /></Icon>
);

export const IconBookmark = (p: IconProps) => (
  <Icon {...p}><path d="M6 4h12v16l-6-4-6 4Z" /></Icon>
);

export const IconBookmarkFilled = (p: IconProps) => (
  <Icon {...p} fill="currentColor"><path d="M6 4h12v16l-6-4-6 4Z" /></Icon>
);

export const IconCart = (p: IconProps) => (
  <Icon {...p}><path d="M3 4h2l2.2 10.4a2 2 0 0 0 2 1.6h7.4a2 2 0 0 0 2-1.55L20 8H6.2" /><circle cx="9.5" cy="19.5" r="1.4" /><circle cx="17" cy="19.5" r="1.4" /></Icon>
);

export const IconUser = (p: IconProps) => (
  <Icon {...p}><circle cx="12" cy="8" r="3.75" /><path d="M4.5 20a7.5 7.5 0 0 1 15 0" /></Icon>
);

export const IconUsers = (p: IconProps) => (
  <Icon {...p}><circle cx="9" cy="8" r="3.25" /><path d="M2.5 19.5a6.5 6.5 0 0 1 13 0" /><path d="M16.5 5.2a3.25 3.25 0 0 1 0 6.3M17.5 13.6a6.5 6.5 0 0 1 4 5.9" /></Icon>
);

export const IconMenu = (p: IconProps) => (
  <Icon {...p}><path d="M4 7h16M4 12h16M4 17h16" /></Icon>
);

export const IconClose = (p: IconProps) => (
  <Icon {...p}><path d="m6 6 12 12M18 6 6 18" /></Icon>
);

export const IconChevronLeft = (p: IconProps) => (
  <Icon {...p}><path d="m14.5 5-7 7 7 7" /></Icon>
);

export const IconChevronRight = (p: IconProps) => (
  <Icon {...p}><path d="m9.5 5 7 7-7 7" /></Icon>
);

export const IconChevronDown = (p: IconProps) => (
  <Icon {...p}><path d="m5 9.5 7 7 7-7" /></Icon>
);

export const IconArrowRight = (p: IconProps) => (
  <Icon {...p}><path d="M4 12h15m-6-6 6 6-6 6" /></Icon>
);

export const IconArrowLeft = (p: IconProps) => (
  <Icon {...p}><path d="M20 12H5m6-6-6 6 6 6" /></Icon>
);

export const IconCheck = (p: IconProps) => (
  <Icon {...p}><path d="m5 12.5 4.5 4.5L19 7" /></Icon>
);

export const IconCheckCircle = (p: IconProps) => (
  <Icon {...p}><circle cx="12" cy="12" r="9" /><path d="m8 12.2 2.7 2.8L16 9.5" /></Icon>
);

export const IconAlert = (p: IconProps) => (
  <Icon {...p}><circle cx="12" cy="12" r="9" /><path d="M12 7.5v5.5" /><circle cx="12" cy="16.3" r="0.9" fill="currentColor" stroke="none" /></Icon>
);

export const IconInfo = (p: IconProps) => (
  <Icon {...p}><circle cx="12" cy="12" r="9" /><path d="M12 11v5.5" /><circle cx="12" cy="7.8" r="0.9" fill="currentColor" stroke="none" /></Icon>
);

export const IconMessage = (p: IconProps) => (
  <Icon {...p}><path d="M20.5 12.2c0 4-3.8 7.2-8.5 7.2a10 10 0 0 1-2.6-.34L4.5 20.5l1.2-3.4A6.9 6.9 0 0 1 3.5 12.2C3.5 8.2 7.3 5 12 5s8.5 3.2 8.5 7.2Z" /></Icon>
);

export const IconMail = (p: IconProps) => (
  <Icon {...p}><rect x="3" y="5" width="18" height="14" rx="2" /><path d="m3.5 7 8.5 6 8.5-6" /></Icon>
);

export const IconTrash = (p: IconProps) => (
  <Icon {...p}><path d="M4.5 7h15M9.5 7V5h5v2M6.5 7l.8 12a2 2 0 0 0 2 1.9h5.4a2 2 0 0 0 2-1.9l.8-12" /></Icon>
);

export const IconPencil = (p: IconProps) => (
  <Icon {...p}><path d="M4 20h4L19 9a2.1 2.1 0 0 0-3-3L5 17Z" /><path d="m14.5 6.5 3 3" /></Icon>
);

export const IconPlus = (p: IconProps) => (
  <Icon {...p}><path d="M12 5v14M5 12h14" /></Icon>
);

export const IconUpload = (p: IconProps) => (
  <Icon {...p}><path d="M12 16V4m0 0L7.5 8.5M12 4l4.5 4.5" /><path d="M4 16v2.5A1.5 1.5 0 0 0 5.5 20h13a1.5 1.5 0 0 0 1.5-1.5V16" /></Icon>
);

export const IconStore = (p: IconProps) => (
  <Icon {...p}><path d="M4 9h16v10a1 1 0 0 1-1 1H5a1 1 0 0 1-1-1Z" /><path d="M3.5 9 5 4h14l1.5 5a2.6 2.6 0 0 1-4.25 2A2.6 2.6 0 0 1 12 11a2.6 2.6 0 0 1-4.25 0A2.6 2.6 0 0 1 3.5 9Z" /><path d="M9.5 20v-5h5v5" /></Icon>
);

export const IconPackage = (p: IconProps) => (
  <Icon {...p}><path d="m12 3 8 4.2v9.6L12 21l-8-4.2V7.2Z" /><path d="m4 7.2 8 4.3 8-4.3M12 11.5V21" /></Icon>
);

export const IconReceipt = (p: IconProps) => (
  <Icon {...p}><path d="M5.5 3h13v18l-2.2-1.5-2.2 1.5-2.1-1.5L9.8 21l-2.1-1.5L5.5 21Z" /><path d="M9 8h6M9 12h6" /></Icon>
);

export const IconChart = (p: IconProps) => (
  <Icon {...p}><path d="M4 20h16" /><path d="M7 20v-6M12 20V6M17 20v-9" /></Icon>
);

export const IconShield = (p: IconProps) => (
  <Icon {...p}><path d="M12 3 5 6v6c0 4.4 3 7.7 7 9 4-1.3 7-4.6 7-9V6Z" /></Icon>
);

export const IconFile = (p: IconProps) => (
  <Icon {...p}><path d="M13 3H7a1.5 1.5 0 0 0-1.5 1.5v15A1.5 1.5 0 0 0 7 21h10a1.5 1.5 0 0 0 1.5-1.5V8.5Z" /><path d="M13 3v5.5h5.5" /></Icon>
);

export const IconCrosshair = (p: IconProps) => (
  <Icon {...p}><circle cx="12" cy="12" r="7" /><circle cx="12" cy="12" r="1.5" fill="currentColor" stroke="none" /><path d="M12 2v3M12 19v3M2 12h3M19 12h3" /></Icon>
);

export const IconSend = (p: IconProps) => (
  <Icon {...p}><path d="M20.5 3.5 10 14" /><path d="M20.5 3.5 14 20.5l-4-6.5-6.5-4Z" /></Icon>
);

export const IconExternal = (p: IconProps) => (
  <Icon {...p}><path d="M14 4h6v6" /><path d="M20 4 11 13" /><path d="M18 14v4.5A1.5 1.5 0 0 1 16.5 20h-11A1.5 1.5 0 0 1 4 18.5v-11A1.5 1.5 0 0 1 5.5 6H10" /></Icon>
);

/**
 * The house mark, used wherever a photograph is missing — an avatar, a product
 * without an image. A bowl of noodles, drawn rather than borrowed from emoji so
 * it takes the surrounding colour and stays crisp at any size.
 */
export const IconBowl = (p: IconProps) => (
  <Icon {...p}>
    <path d="M3.5 10.5h17a8.5 8.5 0 0 1-8.5 8.5 8.5 8.5 0 0 1-8.5-8.5Z" />
    <path d="M8 7.5c0-1.2 1-1.6 1-2.6M12 7c0-1.5 1-2 1-3.2M16 7.5c0-1.2 1-1.6 1-2.6" />
    <path d="M6 19.5h12" />
  </Icon>
);
