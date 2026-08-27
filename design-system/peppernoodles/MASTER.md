# Design System Master File

> **LOGIC:** When building a specific page, first check `design-system/pages/[page-name].md`.
> If that file exists, its rules **override** this Master file.
> If not, strictly follow the rules below.

---

**Project:** PepperNoodles
**Generated:** 2026-08-26 01:00:18
**Category:** General
**Design Dials:** Variance 3/10 (Centered / Minimal) | Motion 3/10 (Subtle) | Density 4/10 (Standard)

---

## Global Rules

### Color Palette

> **PROJECT OVERRIDE.** The generator proposed a navy/blue "professional" palette.
> `CLAUDE.md` §5.3a mandates the 2021 胡椒MAP brand and forbids substituting a
> generic palette, so the brand hues below are authoritative. Elegance is carried
> by the neutral ramp, whitespace and typography — not by new hues.

| Role | Hex | CSS Variable |
|------|-----|--------------|
| Brand hue (accents, rings, map pin) | `#ff3d1c` | `--color-pepper` |
| Filled controls | `#d92f11` | `--color-pepper-fill` |
| Filled hover/active | `#b82609` | `--color-pepper-dark` |
| Inline links / small text | `#c22d10` | `--color-pepper-ink` |
| Primary tint | `#fff1ee` | `--color-pepper-tint` |
| Accent (script/hero) | `#31ff7a` | `--color-mint` |
| Accent (gold) | `#a67c45` | `--color-gold` |
| Foreground | `#141210` | `--color-ink` |
| Body copy | `#57534e` | `--color-body` |
| Background | `#ffffff` | `--color-surface` |
| Section background | `#faf8f6` | `--color-mist` |
| Card | `#ffffff` | `--color-card` |
| Border | `#e7e2dd` | `--color-line` |
| Border (strong) / decorative marks | `#d6cfc7` | `--color-line-strong` |
| Destructive | `#b42318` | `--color-danger` |
| Focus ring | `#ff3d1c` | (`:focus-visible` outline) |

**Color Notes:** Warm-neutral ramp (stone-based, not slate) so the pepper red reads
as appetite rather than alarm. `--color-pepper` is reserved for primary action and
brand marks; destructive actions use `--color-danger` `#b42318` so "delete" is never
confused with "buy".

**Contrast — measured, not assumed.** White on `#ff3d1c` is **3.54:1**, and a 14px bold
button label is *not* large text (that exemption starts at 18.66px bold), so the brand
red cannot carry a button label on its own. The hue is therefore kept for everything
where contrast is satisfied or irrelevant — focus rings, the map pin, chip borders,
`::after` underlines, large display type — while **filled controls use
`--color-pepper-fill` `#d92f11` (4.81:1 with white)**, hovering to `#b82609` (6.33:1).
Inline body links use `--color-pepper-ink` `#c22d10` (4.9:1 on white).

There are exactly **two text greys**, not three. An earlier draft added a third
(`#a8a29e`) "for decorative use only"; within a day it was carrying timestamps, phone
numbers and stock counts at 2.5:1. Anything that means something uses
`--color-subtle`; anything purely decorative and `aria-hidden` uses
`--color-line-strong`, which is a border colour and looks like one.

Every page is checked against WCAG AA with a scripted audit (see "Verification").

### Typography

> **PROJECT OVERRIDE.** Playfair Display SC / Karla are dropped: neither has a
> 繁體中文 glyph set, so every Chinese character would fall back mid-sentence.
> The 2021 display faces are kept per `CLAUDE.md` §5.3a, paired with the platform's
> native CJK face for body copy (zero download, correct 繁體 shaping).

- **Display / headings:** Sulphur Point (`--font-display`) — 2021 brand face
- **Script kicker:** Sacramento (`--font-script`) — 2021 brand face, decorative only
- **Body:** system CJK stack (`--font-sans`) — PingFang TC → Noto Sans TC → Microsoft JhengHei
- **Numeric:** tabular figures on prices, counts and table columns
- **Mood:** restaurant, menu, culinary, elegant, foodie, hospitality

**Type scale** (1.25 major-third, 16px base):

| Token | Size / line-height | Usage |
|-------|--------------------|-------|
| `--text-display` | 48–72px / 1.05 | Hero only |
| `--text-h1` | 32px / 1.2 | Page title |
| `--text-h2` | 24px / 1.3 | Section title |
| `--text-h3` | 18px / 1.4 | Card title |
| `--text-body` | 16px / 1.65 | Body copy, max 68ch |
| `--text-sm` | 14px / 1.6 | Secondary |
| `--text-xs` | 12px / 1.5 | Meta only — never body |

### Spacing Variables

*Density: 4/10 — Standard*

| Token | Value | Usage |
|-------|-------|-------|
| `--space-xs` | `4px` / `0.25rem` | Tight gaps |
| `--space-sm` | `8px` / `0.5rem` | Icon gaps, inline spacing |
| `--space-md` | `16px` / `1rem` | Standard padding |
| `--space-lg` | `24px` / `1.5rem` | Section padding |
| `--space-xl` | `32px` / `2rem` | Large gaps |
| `--space-2xl` | `48px` / `3rem` | Section margins |
| `--space-3xl` | `64px` / `4rem` | Hero padding |

### Shadow Depths

| Level | Value | Usage |
|-------|-------|-------|
| `--shadow-sm` | `0 1px 2px rgba(0,0,0,0.05)` | Subtle lift |
| `--shadow-md` | `0 4px 6px rgba(0,0,0,0.1)` | Cards, buttons |
| `--shadow-lg` | `0 10px 15px rgba(0,0,0,0.1)` | Modals, dropdowns |
| `--shadow-xl` | `0 20px 25px rgba(0,0,0,0.15)` | Hero images, featured cards |

---

## Component Specs

### Buttons

```css
/* Primary Button */
.btn-primary {
  background: #0369A1;
  color: white;
  padding: 12px 24px;
  border-radius: 8px;
  font-weight: 600;
  transition: all 200ms ease;
  cursor: pointer;
}

.btn-primary:hover {
  opacity: 0.9;
  transform: translateY(-1px);
}

/* Secondary Button */
.btn-secondary {
  background: transparent;
  color: #0F172A;
  border: 2px solid #0F172A;
  padding: 12px 24px;
  border-radius: 8px;
  font-weight: 600;
  transition: all 200ms ease;
  cursor: pointer;
}
```

### Cards

```css
.card {
  background: #F8FAFC;
  border-radius: 12px;
  padding: 24px;
  box-shadow: var(--shadow-md);
  transition: all 200ms ease;
  cursor: pointer;
}

.card:hover {
  box-shadow: var(--shadow-lg);
  transform: translateY(-2px);
}
```

### Inputs

```css
.input {
  padding: 12px 16px;
  border: 1px solid #E2E8F0;
  border-radius: 8px;
  font-size: 16px;
  transition: border-color 200ms ease;
}

.input:focus {
  border-color: #0F172A;
  outline: none;
  box-shadow: 0 0 0 3px #0F172A20;
}
```

### Modals

```css
.modal-overlay {
  background: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(4px);
}

.modal {
  background: white;
  border-radius: 16px;
  padding: 32px;
  box-shadow: var(--shadow-xl);
  max-width: 500px;
  width: 90%;
}
```

---

## Style Guidelines

**Style:** Minimalism & Swiss Style

**Keywords:** Clean, simple, spacious, functional, white space, high contrast, geometric, sans-serif, grid-based, essential

**Best For:** Enterprise apps, dashboards, documentation sites, SaaS platforms, professional tools

**Key Effects:** Subtle hover (200-250ms), smooth transitions, sharp shadows if any, clear type hierarchy, fast loading

### Page Pattern

**Pattern Name:** Hero + Features + CTA

- **Conversion Strategy:** Deep CTA placement. For CTA label text, verify at least 4.5:1 against the button fill; use 7:1 only when the product explicitly targets AAA normal-text contrast. Keep focus and component boundaries independently visible. Disable hero parallax under reduced motion and render its static final state.
- **CTA Placement:** Hero (sticky) + Bottom
- **Section Order:** Hero with headline/image > Value prop > Key features (3-5) > CTA section > Footer

---

## Motion

**Scroll Reveal** (Subtle) — Trigger: scroll (viewport enter) | Duration: 300-400ms | Easing: `power1.out`

```js
gsap.from(el, { opacity: 0, y: 12, duration: 0.35, ease: 'power1.out', scrollTrigger: { trigger: el, start: 'top 90%', toggleActions: 'play none none reverse' } });
```

**Framework notes:** Requires the ScrollTrigger plugin registered once via gsap.registerPlugin(ScrollTrigger); Use matchMedia('(prefers-reduced-motion: reduce)') to skip non-essential motion and render the final state immediately

- ✅ Keep the y offset small (8-16px) so it reads as a fade, not a slide
- ❌ Don't reveal below-the-fold content needed for SEO/crawlers as invisible-by-default without a no-JS fallback
- ⚡ toggleActions 'play none none reverse' avoids re-triggering on every scroll direction change

---

## Anti-Patterns (Do NOT Use)


### Additional Forbidden Patterns

- ❌ **Emojis as icons** — Use SVG icons (Heroicons, Lucide, Simple Icons)
- ❌ **Missing cursor:pointer** — All clickable elements must have cursor:pointer
- ❌ **Layout-shifting hovers** — Avoid scale transforms that shift layout
- ❌ **Low contrast text** — Maintain 4.5:1 minimum contrast ratio
- ❌ **Instant state changes** — Always use transitions (150-300ms)
- ❌ **Invisible focus states** — Focus states must be visible for a11y

---

## Pre-Delivery Checklist

Before delivering any UI code, verify:

- [ ] No emojis used as icons (use SVG instead)
- [ ] All icons from consistent icon set (Heroicons/Lucide)
- [ ] `cursor-pointer` on all clickable elements
- [ ] Hover states with smooth transitions (150-300ms)
- [ ] Light mode: text contrast 4.5:1 minimum
- [ ] Focus states visible for keyboard navigation
- [ ] `prefers-reduced-motion` respected
- [ ] Responsive: 375px, 768px, 1024px, 1440px
- [ ] No content hidden behind fixed navbars
- [ ] No horizontal scroll on mobile


---

## Responsive rules

The site is responsive at every width from **320px to 1920px**. The rules that
make it so, in the order they matter:

| Rule | Why |
|---|---|
| **One breakpoint does the heavy lifting: `lg` (1024px).** Above it the nav is inline and multi-column layouts open up; below it the nav is a drawer and columns stack. | Ten inline nav links overflowed the viewport under ~1100px. |
| **`sm` (640px) is the phone/not-phone line.** Tables reflow, filter rails collapse, and tap targets relax above it. | 640px is where a two-column card grid and a real table both start to fit. |
| **Minimum text size is 12px.** No exceptions, including third-party map chrome. | 11px timestamps and badges were unreadable on a phone. |
| **Tap targets are 44px on phones**, shrinking to their natural inline height from `sm:` up via `min-h-11 sm:min-h-0`. | A 16px-tall footer link is a mouse target, not a thumb target. |
| **Data tables render twice**: a labelled card list below `sm`, the real `<table>` above it. See `DataTable`. | A 576px-wide grid inside a horizontal scroller is *technically* responsive and unusable in practice — the column headings scroll out of view. |
| **User-supplied strings get `break-words`.** | `visitor.1787680160868@example.com` has no break opportunity and ran under the button beside it. |
| **Nothing scrolls sideways.** Wide content scrolls inside its own container, never the document. | |
| **Inline links inside a sentence are exempt from the target rule** (WCAG 2.5.8). | The sentence sets the line height; padding a link inside prose breaks the paragraph. |

Two things are deliberately *not* responsive: the `hero-overlay` scrim and the
`search-ring` radius change at `sm` — both are visual, not layout, and are
described in `globals.css`.

## Verification

The redesign was checked against the running stack, not by eye alone:

- **60 Playwright end-to-end tests** covering every flow — all pass.
- **A route-wide audit** across all 32 routes at 375 / 768 / 1440 px asserting no
  horizontal overflow, an accessible name on every interactive control, and no
  console errors. 96/96.
- **A scripted WCAG AA contrast pass** over 18 representative pages, comparing every
  text node against its resolved background at the correct threshold for its size
  and weight. It is what caught the `#ff3d1c` button label and the third grey.
- **A responsive audit** over 30 routes × 7 widths (320 / 375 / 414 / 768 / 1024 /
  1280 / 1920) asserting: no sideways scroll, no element past the viewport edge, no
  media wider than the viewport, no `min-width` exceeding it, no text under 12px, no
  tap target under 40px on phone widths, and no text wider than its own container.
  **210/210.** It went from 108/210 on the first run — the gap was tap targets and
  the non-reflowing tables.

Both audits were scratch harnesses and were removed after use; re-create them from
this file's rules if the design changes materially.

## Decisions worth not re-litigating

1. **Icons are SVG, never emoji.** 🍜 as a missing-image placeholder, 🎉 on campaigns
   and ★/☆/✕/✓ as text all went. Emoji render differently per platform, cannot be
   recoloured, and are announced by their CLDR name.
2. **Below `lg` the nav is a drawer.** Ten inline links overflowed the viewport
   under ~1100px and scrolled the document sideways.
3. **Labels are always visible.** A placeholder disappears exactly when the user
   still needs to know what the field was.
4. **Loading reserves the real shape.** Skeletons, not a page-replacing spinner.
5. **Destructive ≠ brand.** Delete uses `--color-danger`, never pepper.
6. **Responsive means reflow, not just "it fits".** A horizontal scrollbar on a
   data table passes an overflow check and still fails the person holding the phone.
