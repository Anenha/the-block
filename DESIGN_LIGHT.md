---
name: Kinetic Apex Light
colors:
  surface: '#f8f9fa'
  surface-dim: '#d8dae1'
  surface-bright: '#f9f9ff'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f1f3f5'
  surface-container: '#e9ecef'
  surface-container-high: '#dee2e6'
  surface-container-highest: '#ced4da'
  on-surface: '#191c21'
  on-surface-variant: '#3e494c'
  inverse-surface: '#2e3036'
  inverse-on-surface: '#eff0f8'
  outline: '#6e797d'
  outline-variant: '#bdc8cc'
  surface-tint: '#00687a'
  primary: '#006577'
  on-primary: '#ffffff'
  primary-container: '#008096'
  on-primary-container: '#f9fdff'
  inverse-primary: '#6cd4ef'
  secondary: '#a73a00'
  on-secondary: '#ffffff'
  secondary-container: '#fd651e'
  on-secondary-container: '#571a00'
  tertiary: '#006948'
  on-tertiary: '#ffffff'
  tertiary-container: '#00855d'
  on-tertiary-container: '#f5fff7'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#adecff'
  primary-fixed-dim: '#6cd4ef'
  on-primary-fixed: '#001f26'
  on-primary-fixed-variant: '#004e5d'
  secondary-fixed: '#ffdbce'
  secondary-fixed-dim: '#ffb599'
  on-secondary-fixed: '#370e00'
  on-secondary-fixed-variant: '#7f2b00'
  tertiary-fixed: '#85f8c4'
  tertiary-fixed-dim: '#68dba9'
  on-tertiary-fixed: '#002114'
  on-tertiary-fixed-variant: '#005137'
  background: '#f9f9ff'
  on-background: '#191c21'
  surface-variant: '#e1e2e9'
  cyan-core: '#00a3c4'
  cyan-deep: '#006d80'
  cyan-container: '#e0f7fa'
  track-amber: '#f97316'
  telemetry-emerald: '#059669'
  text-primary: '#0e1116'
  text-secondary: '#4a5568'
  text-muted: '#64748b'
  border-subtle: rgba(14, 17, 22, 0.08)
  border-strong: rgba(14, 17, 22, 0.16)
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

This design system establishes the daytime, high-luminance companion experience for high-performance electric mobility. It channels the clinical luxury of architectural automotive design studios, wind tunnel testing bays, and sunlit racing circuits. Instead of mimicking standard sterile consumer interfaces, it captures the raw velocity, mechanical poise, and bespoke engineering of electric performance grand tourers.

The design movement combines **Engineering Minimalism** with **Prismatic Precision**. The UI is built on stark, pure-white gallery backdrops paired with multi-tiered anodized metallic grays, razor-sharp geometric typography, and laser-focused electric cyan performance cues. The emotional tone is authoritative, hyper-focused, and premium. Interactions feel immediate, mechanical, and tactile—evoking precision-milled aluminum switches, active aero indicators, and digital track-side telemetry displays under full daylight.

## Colors

The light mode color palette shifts from the cockpit's nocturnal darkness to an ultra-crisp, gallery-grade daylight environment while retaining maximum chromatic punch and legible contrast.

- **Primary (`#008FA8`)**: "Performance Cyan" — Calibrated specifically for high-contrast legibility on light surfaces, exceeding WCAG AAA standards for headers and AA for interactive elements. Paired with dynamic vibrant Cyan (`#00A3C4`) for accents, and deep Cyan (`#006D80`) for active pressed states.
- **Secondary (`#EA580C`)**: "Track Amber" — Carried over directly from performance brake calipers and acceleration telemetry. Highlights track pack configurations, boost states, and urgent vehicle diagnostics.
- **Tertiary (`#059669`)**: "Telemetry Emerald" — Represents regenerative braking efficiency, supercharger throughput, optimal battery operating temps, and live network sync.
- **Neutral Canvas Hierarchy (`#0E1116`)**:
  - `surface-container-lowest` (`#FFFFFF`): Pristine white staging ground for high-resolution vehicle renders and hero cards.
  - `surface` (`#F8F9FA`): Foundation canvas providing a light neutral stage.
  - `surface-container-low` (`#F1F3F5`): Low-tier container grouping for passive dashboard panels.
  - `surface-container` (`#E9ECEF`): Inset spec sheets, inactive switches, and chip backgrounds.
  - `surface-container-high` (`#DEE2E6`): Secondary control surfaces and segmented control tracks.
  - `text-primary` (`#0E1116`): High-carbon deep black delivering absolute visual weight.
  - `text-secondary` (`#4A5568`): Graphite gray for secondary specs, units, and supporting metadata.
  - `text-muted` (`#64748B`): Slate gray for legal copy, disabled states, and auxiliary labels.

## Typography

The typographic hierarchy retains the dual-font strategy: technical precision via **Space Grotesk** coupled with the refined clarity of **Outfit**.

- **Space Grotesk**: Deployed for display titles, vehicular model designations, speed and performance telemetry (`metric-val`), system states, and micro-labels. In this high-luminance environment, its tight geometric proportions and crisp ink-traps project an exacting, wind-tunnel-tested industrial tone.
- **Outfit**: Utilized across running text, spec sheet details, powertrain descriptions, and dealership configurations. Its open geometric bowls ensure effortless readability against high-key daylight backdrops.
- **Telemetry Readouts**: Metrics utilize `metric-val` in direct lockup with uppercase `label-sm` unit badges (e.g., "0-60 MPH", "NM TORQUE", "KWH CONSUMPTION"). These maintain tabular numeral styling to avoid layout shifting during live telemetry streams.

## Layout & Spacing

Layout geometry follows an architectural grid structure calibrated to showcase vehicle proportions and dense performance data.

- **Grid Architecture**: 
  - Mobile (viewport &lt; 768px): 4-column layout with `0.75rem` (`12px`) gutters and `1rem` (`16px`) outer canvas margins.
  - Tablet (768px – 1024px): 8-column system with `1rem` (`16px`) gutters and `1.5rem` (`24px`) margins.
  - Desktop (&gt; 1024px): 12-column layout maxing out at `1440px` centered canvas with `1.5rem` gutters.
- **Showroom Stage Ratio**: Hero configuration modules honor an ultra-wide 21:9 or panoramic 16:9 proportion, stretching edge-to-edge on mobile viewports to let vehicle silhouettes breathe without boundary clipping.
- **Telemetry Cluster Spacing**: Data groupings use strict `space-xs` (4px) and `space-sm` (8px) internal micro-spacing to ensure telemetry parameters feel unified, while sibling telemetry modules separate along `space-md` (16px) boundaries.

## Elevation & Depth

In a clean, daytime lighting model, depth is achieved not with muddy drop shadows, but through **Tonal Stacking**, **Frosted White Specular Glass**, and **Micro-Outlines**.

1. **Base Ground (L0)**: Canvas background (`#F8F9FA`).
2. **Structural Panels (L1)**: Pure white cards (`#FFFFFF`) defined with structural micro-borders (`1px solid rgba(14, 17, 22, 0.08)`) and an imperceptible diffused floor shadow (`0 2px 8px -2px rgba(14, 17, 22, 0.04), 0 1px 2px rgba(14, 17, 22, 0.02)`).
3. **Floating Floating Controls / Navigation (L2)**: Semi-translucent crystalline surfaces (`rgba(255, 255, 255, 0.88)`) combined with a `20px` backdrop blur, a light specular top highlight (`inset 0 1px 0 rgba(255, 255, 255, 0.9)`), and an ambient directional shadow (`0 12px 32px -4px rgba(14, 17, 22, 0.08)`).
4. **Active Cyan Field (Interaction)**: Interactive focus rings and selected telemetry clusters do not glow neon; instead, they project a focused, high-clarity cyan aura (`box-shadow: 0 0 0 3px rgba(0, 143, 168, 0.20), 0 4px 12px rgba(0, 143, 168, 0.12)`).

## Shapes

The design system maintains the signature 8px (`0.5rem`) primary corner radius, expressing structural engineering discipline:

- **Base Cards & Control Inputs**: Exact `0.5rem` (`8px`) border radius. This geometry echoes machined aluminum vehicle sub-assemblies.
- **Drawer Panels & Bottom Sheets**: Scaled to `rounded-lg` (`1rem` / `16px`) and `rounded-xl` (`1.5rem` / `24px`) along top edges to fit device physical contours.
- **Pill Attributes**: Fully circular (`rounded-full` / `9999px`) styling is restricted exclusively to status indicators, drive mode pills (SPORT, TRACK, ECO), and floating action buttons.

## Components

### Buttons
- **Primary Action (Reserve Vehicle / Order Drive)**: High-contrast Performance Cyan solid fill (`#008FA8`) with pure white text (`#FFFFFF`) rendered in Space Grotesk Bold. Hover state darkens to `#006D80`; active state depresses by `scale(0.98)`.
- **Secondary Action (Technical Spec / Build & Price)**: Pristine white container (`#FFFFFF`) with a sharp structural outline (`1px solid #DEE2E6`), deep black text (`#0E1116`), transitioning on hover to `#F1F3F5` surface tint.
- **Tertiary / Ghost (Compare Trims)**: Transparent background with `#008FA8` text and an animated underline on hover.

### Chips & Telemetry Tags
- **Drive Mode Chips**: Surface container pill (`#E9ECEF`) housing an uppercase `label-md`. Active "TRACK" mode switches to `#EA580C` text with `#FEECE5` background; active "ELECTRIC" mode uses `#008FA8` text with `#E0F7FA` container.
- **Telemetry Indicators**: Compact rectangular badge with 4px radius, displaying battery state of charge or thermal warnings with 6px status pips.

### Metric Panels (Telemetry Grid)
- Two-column or four-column modular blocks built on `#FFFFFF`.
- Features an ultra-light top edge separator (`2px solid #E9ECEF`).
- Label displayed above value in uppercase `label-sm` (`#64748B`), with large scalar values rendered in `metric-val` (`#0E1116`) paired with an aligned unit tag (e.g., `s`, `kW`, `bhp`).

### Cards & Vehicles Displays
- **Showroom Stage Card**: Pure white surface (`#FFFFFF`) with `8px` corner radius, encased by `1px solid rgba(14, 17, 22, 0.08)`. Features an edge-to-edge aspect ratio vehicle view, top-left glassmorphic price badge, top-right wishlist pill, and a lower telemetry dock highlighting 0-60 and range values.

### Form Inputs & Search Fields
- Crisp `#F8F9FA` fill with inset `1px solid #DEE2E6` perimeter border. Text entered appears in high-contrast `#0E1116`.
- Focus state instantly swaps the border to `2px solid #008FA8` backed by an ambient cyan halo.

### Floating Bottom Navigation Dock
- Grounded floating pill anchored `16px` above the bottom screen edge. Made of crystalline frosted white (`rgba(255, 255, 255, 0.90)` + `blur(20px)`), with a defined 1px graphite rim (`rgba(14, 17, 22, 0.06)`). Active icons illuminate in `#008FA8` with a micro-indicator bar underneath.