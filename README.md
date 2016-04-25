**AudioSpeakerDiarization**
============================================================

##Note

An android project for speaker diarization by speech

'Note again' The project with better performance is in [SIDV5.0](https://github.com/KangCai/AudioSpeakerDiarizationV5.0/)

A simple android app for real-time speaker diarization. As far as I'm concerned, there is no real-time speaker diarizatino tool now. But I have to admit the performance of this version is far from pratical application, because the method is LPC + MFCC / Euclidean Distance. My next plan is to implement a system with the state of the art method, i-vector + PLDA.


##Usage

You can use this app by the following three steps.
  1. Process of creating user. Input several (>=1) pairs of name (String) and audio recording. I have realized the recording function in the app. You should push the first button to start recording and push the second button to end it. And while you push the second button, a pair has been created in the database (in fact a file).
  2. Process of recognition. Push the the button to start and push the third button to end it. And while you push the third button, the recognition results will be shown in the display board.

##For Developers.
  1. MainAcitivity.java is at /src/com/audiorecordtest/MainActivity.java.
  2. You can also get to start by reference to /src/com/audioprocessingbox/myfunc/AppSIDDemo.java.
