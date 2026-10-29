---
name: Kinetic Apex
colors:
  surface: '#111317'
  surface-dim: '#111317'
  surface-bright: '#37393d'
  surface-container-lowest: '#0c0e11'
  surface-container-low: '#1a1c1f'
  surface-container: '#1e2023'
  surface-container-high: '#282a2d'
  surface-container-highest: '#333538'
  on-surface: '#e2e2e6'
  on-surface-variant: '#b9cacb'
  inverse-surface: '#e2e2e6'
  inverse-on-surface: '#2f3034'
  outline: '#849495'
  outline-variant: '#3b494b'
  surface-tint: '#00dbe9'
  primary: '#dbfcff'
  on-primary: '#00363a'
  primary-container: '#00f0ff'
  on-primary-container: '#006970'
  inverse-primary: '#006970'
  secondary: '#ffb68b'
  on-secondary: '#522300'
  secondary-container: '#ff7f1c'
  on-secondary-container: '#602a00'
  tertiary: '#d8ffe7'
  on-tertiary: '#003824'
  tertiary-container: '#65f2b5'
  on-tertiary-container: '#006d4a'
  error: '#ffb4ab'
  on-error: '#690005'
  error-container: '#93000a'
  on-error-container: '#ffdad6'
  primary-fixed: '#7df4ff'
  primary-fixed-dim: '#00dbe9'
  on-primary-fixed: '#002022'
  on-primary-fixed-variant: '#004f54'
  secondary-fixed: '#ffdbc8'
  secondary-fixed-dim: '#ffb68b'
  on-secondary-fixed: '#321200'
  on-secondary-fixed-variant: '#753400'
  tertiary-fixed: '#6ffbbe'
  tertiary-fixed-dim: '#4edea3'
  on-tertiary-fixed: '#002113'
  on-tertiary-fixed-variant: '#005236'
  background: '#111317'
  on-background: '#e2e2e6'
  surface-variant: '#333538'
typography:
  display-lg:
    fontFamily: Space Grotesk
    fontSize: 44px
    fontWeight: '700'
    lineHeight: 52px
    letterSpacing: -0.03em
  display-lg-mobile:
    fontFamily: Space Grotesk
    fontSize: 34px
    fontWeight: '700'
    lineHeight: 40px
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Space Grotesk
    fontSize: 32px
    fontWeight: '600'
    lineHeight: 38px
    letterSpacing: -0.02em
  headline-lg-mobile:
    fontFamily: Space Grotesk
    fontSize: 26px
    fontWeight: '600'
    lineHeight: 32px
    letterSpacing: -0.01em
  headline-md:
    fontFamily: Space Grotesk
    fontSize: 22px
    fontWeight: '600'
    lineHeight: 28px
    letterSpacing: -0.01em
  headline-sm:
    fontFamily: Space Grotesk
    fontSize: 18px
    fontWeight: '600'
    lineHeight: 24px
  body-lg:
    fontFamily: Outfit
    fontSize: 18px
    fontWeight: '400'
    lineHeight: 28px
  body-md:
    fontFamily: Outfit
    fontSize: 15px
    fontWeight: '400'
    lineHeight: 22px
  body-sm:
    fontFamily: Outfit
    fontSize: 13px
    fontWeight: '400'
    lineHeight: 18px
  label-lg:
    fontFamily: Space Grotesk
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 18px
    letterSpacing: 0.04em
  label-md:
    fontFamily: Space Grotesk
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
    letterSpacing: 0.05em
  label-sm:
    fontFamily: Space Grotesk
    fontSize: 10px
    fontWeight: '600'
    lineHeight: 14px
    letterSpacing: 0.08em
  metric-val:
    fontFamily: Space Grotesk
    fontSize: 24px
    fontWeight: '700'
    lineHeight: 28px
    letterSpacing: -0.02em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  gutter: 1rem
  gutter-mobile: 0.75rem
  margin: 1.5rem
  margin-mobile: 1rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 2rem
---

## Brand & Style

This design system embodies the thrill, precision, and engineering perfection of modern automotive performance. Built for an immersive mobile-first vehicle discovery and marketplace platform, it merges high-end automotive editorial energy with clinical technical clarity. 

The aesthetic is precision-engineered minimalism enhanced with technical glassmorphism. It evokes confidence, velocity, and luxury—reminiscent of bespoke digital cockpits and performance telemetry systems. Interfaces are characterized by deep, carbon-steeped canvases, high-contrast typography, and luminescent accents that channel electric powertrain energy. Interactions feel direct, responsive, and tactile, elevating car shopping into an enthusiast-grade exploration experience.

## Colors

The palette establishes an aggressive, high-contrast dark environment inspired by carbon fiber monocoques, dark tinted glass, and cockpit instrumentation:

- **Primary (`#00F0FF`)**: "Hyper Cyan" — An electric, high-voltage cyan serving as the primary interactive cue, selected vehicle states, telemetry highlights, and main call-to-actions.
- **Secondary (`#FF7A00`)**: "Apex Amber" — An energetic, combustion-inspired warm accent used for performance badges, pricing spotlights, alert states, and acceleration/horsepower callouts.
- **Tertiary (`#10B981`)**: "Kinetic Mint" — Dedicated to battery efficiency, instant availability, verified badges, and eco-performance ratings.
- **Neutral (`#0D0F12`)**: "Deep Carbon" — The foundational pitch-black canvas, layered upward with calibrated slate surfaces:
  - Base Canvas: `#090A0D`
  - Elevated Surface: `#13161C`
  - Container / Card: `#1A1E26`
  - Border / Ghost Outlines: `rgba(255, 255, 255, 0.08)`
  - Text Primary: `#F3F4F6`
  - Text Secondary: `#9CA3AF`
  - Text Muted: `#6B7280`

## Typography

The type system blends the technical, cockpit-like precision of **Space Grotesk** with the fluid, legible geometry of **Outfit**.

- **Space Grotesk** is reserved for headlines, key vehicle telemetry (0-60 times, BHP, range, torque), filter tags, and status labels. Its subtle ink-traps and mechanical angles echo precision machine calibration.
- **Outfit** carries all body copy, long-form editorial reviews, seller notes, and disclaimers. Its balanced geometric construction maintains pristine legibility against low-luminance backdrops.
- Key specification figures use the dedicated `metric-val` style combined with uppercase `label-sm` descriptors to mimic race car telemetry clusters.

## Layout & Spacing

The layout model is anchored around mobile ergonomics, using a fluid 4-column grid on phones and reflowing to an 8-column layout on tablets and 12-column on desktop inspection dashboards.

- **Thumb Zone Anchoring**: Primary action targets (filter sheets, buy/reserve triggers, 360-view toggles) are persistently anchored to the bottom 25% of the viewport.
- **Spec Grid Rhythm**: Vehicle specification modules utilize tight `space-sm` internal padding and `space-md` gaps to present dense, glanceable telemetry without cognitive overload.
- **Edge-to-Edge Visuals**: Hero vehicle galleries bypass canvas margins (`margin-mobile`), spanning full bleed horizontally to maximize vehicle silhouette impact.

## Elevation & Depth

Visual hierarchy is maintained through disciplined tonal staging and localized glassmorphism rather than muddy drop shadows.

1. **Base Layer (L0)**: Deep carbon background (`#090A0D`) providing maximum contrast for vehicle exterior photography.
2. **Surface Layer (L1)**: Elevated containers (`#13161C`) paired with a micro-thin rim highlight (`1px solid rgba(255, 255, 255, 0.05)`).
3. **Glass Cockpit Floating Layer (L2)**: Semi-transparent surfaces (`rgba(22, 27, 34, 0.72)`) backed by a `16px` backdrop-blur filter and an upper-edge inner specular highlight (`inset 0 1px 0 rgba(255, 255, 255, 0.12)`). Used exclusively for the bottom navigation dock, sticky pricing bars, and floating media controls.
4. **Active Instrument Glow**: Highlighted elements generate an ultra-diffuse, chromatic cyan aura (`box-shadow: 0 0 24px rgba(0, 240, 255, 0.18)`), creating an illuminated, high-tech instrument cluster feel.

## Shapes

The interface balances aerodynamic curves with structural stability using a `Rounded` (Level 2) shape philosophy:

- Standard cards, interactive tiles, and inputs utilize `0.5rem` (`8px`) corner radiuses for an engineered, precise feel.
- Floating bottom drawers, modal sheets, and primary vehicle hero containers scale to `rounded-lg` (`1rem` / `16px`) and `rounded-xl` (`1.5rem` / `24px`) to gracefully cradle mobile screen corners.
- Status chips, fuel/charge badges, and circular action toggles (favorite, share, audio playback) utilize full pill/circle radii to contrast sharply against rigid layout grids.

## Components

### Buttons
- **Primary Action (Reserve / Test Drive)**: High-voltage Cyan background (`#00F0FF`) with pure black Space Grotesk bold text (`#000000`). Pressed state scales slightly down (`scale(0.98)`) with a subtle glow intensification.
- **Secondary (Inspect Specs / Contact Dealer)**: Dark slate translucent fill with a crisp `1px solid rgba(255, 255, 255, 0.14)` border and high-contrast white text.
- **Icon Buttons (Favorite / 360° / Sound Check)**: 44x44px circular glass pill with backdrop blur and low-contrast border.

### Status & Spec Chips
- Compact, uppercase Space Grotesk labels with high-saturation indicator pips.
- **EV/Hybrid Status**: Bordered badge in `rgba(0, 240, 255, 0.15)` with glowing cyan pip.
- **Performance / Track Pack**: Amber-tinted fill (`rgba(255, 122, 0, 0.15)`) with bright orange typography.
- **Market Price Alert ("Great Price")**: Emerald fill with high-contrast text.

### Vehicle Spec Metric Blocks
- Modular micro-cards displaying key stats (0-60 mph, Range/MPG, Peak Power). Top label in `label-sm` muted uppercase; large numeric value in `metric-val` Space Grotesk, positioned over a subtle tonal surface with a soft top border highlight.

### Cards
- **Vehicle Listing Card**: Full-width card with a 16:9 photography anchor, rounded top corners, inset glassmorphic badge overlays (mileage, year, certified status), and an integrated bottom telemetry bar showing monthly financing and instant cash pricing.

### Inputs & Search
- Integrated search bar with dark obsidian surface, inset search icon, and quick-filter micro chips embedded directly beneath the active search field. Focused states ignite a 1px Cyan perimeter.

### Bottom Navigation Dock
- Floating, detached island with heavy backdrop blur, elevated above the bottom edge with `16px` margin. Houses 4 primary destinations with glowing icon-state indicators and active cyan tracking lights beneath the current view.