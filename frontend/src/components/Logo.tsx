import Image from "next/image";

/**
 * The 2021 PepperNoodle mark.
 *
 * <p>The source PNG is a salmon roundel with a <em>white</em> wordmark beneath
 * it, drawn for the dark hero. On a light header that wordmark disappears, so
 * there the roundel is cropped out of the artwork and paired with a text
 * wordmark instead of showing the invisible one.
 *
 * <p>The crop numbers below map the roundel — which occupies roughly
 * x 55–145, y 22–112 of the 200×200 source — into a square box.
 */
const MARK = { size: 200, x: 55, y: 22, edge: 90 } as const;

export function Logo({ onDark }: { onDark: boolean }) {
  if (onDark) {
    // Over the hero photograph the original artwork reads correctly as-is.
    return (
      <Image
        src="/brand/logo.png"
        alt="PepperNoodle"
        width={132}
        height={52}
        priority
        className="h-12 w-auto"
      />
    );
  }

  const box = 40;
  const scale = box / MARK.edge;

  return (
    <span className="flex items-center gap-2.5">
      <span
        aria-hidden
        className="relative block shrink-0 overflow-hidden"
        style={{ width: box, height: box }}
      >
        <Image
          src="/brand/logo.png"
          alt=""
          width={MARK.size}
          height={MARK.size}
          priority
          style={{
            position: "absolute",
            width: MARK.size * scale,
            height: MARK.size * scale,
            maxWidth: "none",
            left: -MARK.x * scale,
            top: -MARK.y * scale,
          }}
        />
      </span>
      <span className="font-display text-xl font-bold tracking-tight text-ink">
        PepperNoodle
      </span>
    </span>
  );
}
