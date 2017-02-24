JFLAGS = -g
JC = javac -encoding UTF-8
JAVA = java
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	ButtonPlay.java ButtonPlayBackward.java ButtonPlayForward.java ButtonSeek.java ButtonSeekEnd.java ButtonSeekNext.java ButtonSeekPrevious.java ButtonSeekStart.java Main.java Model.java Observer.java PaintingClipboard.java PlayTimer.java StrokeStruct.java View.java ViewCanvas.java ViewColorPalette.java ViewColorPreview.java ViewPlaybackControls.java ViewStrokeControl.java

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class

run: default
	$(JAVA) -cp . Main
