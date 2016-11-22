# Changelog

## v0.1.2 (22/11/16)

## Fixes

- Removed the animators, which were causing views to not return to their original position after Snackbars were swiped off the screen.

## v0.1.1 (05/09/16)

### API Changes

- Made the `ViewProvider` interface public, to allow you to override the behavior to specify which views to translate.

## v0.1.0 (05/09/16)

First development release.

### Features

- Behaviour you can attach to views to move them out of the way of Snackbars.
- Can extend the behaviour to specify which child views to move out of the way (by default it will just move the view that the behaviour is attached to).

### Dependencies

- Target API 24
- Minimum API 15
- Support library v24.2.0

## v0.1.1 (05/09/16)

### API Changes

- Made the `ViewProvider` interface public, to allow you to override the behavior to specify which views to translate.
