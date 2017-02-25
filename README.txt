Description:

A paint application with built-in animation support.

Features:

- 7 default colors to choose from the color palette, where the 8th button (with
  the "?") allows you to choose your own color through a ColorChooser.
- The current color of your brush.
- A stroke width slider, scaling from 1 to 15 whole numbers.
- Playback controls. These are disabled if nothing has been drawn yet. Once st-
  rokes are added to the canvas, these controls unlock and allow the following
  functionality:
  - Slider to scroll through approximately every point drawn. It is approximate
    because the granularity depends on the stroke with the most points.
  - Go-to-start button, to go to the start of the animation.
  - Go-to-previous-stroke button, to go to the nearest previous stroke
    start/end.
  - Play backward button, to play through the animation backward starting at
    the current frame.
  - Play forward button, analogous to Play backward button.
  - Go-to-next-stroke button, analogous to Go-to-previous-stroke button.
  - Go-to-end button, analogous to Go-to-start button.
- File save/load:
    Save the animation, including current stroke width, current color, and
    current position in the playback bar.
- New/Exit:
    New creates a new canvas. Exit exits the program.
    Note that New/Load/Exit can show a prompt asking to save if any unsaved
    changes have been made to the current canvas.
- Clipboard support:
    Allows copying the currently-viewed frame to the system's clipboard.
- Tooltips:
    Most buttons/options have tooltips to read if anything is unclear.

Development environment:

Windows 8.1, using javac 1.8.0_121 and java 1.8.0.121
Tested Make commands on Manjaro Linux
