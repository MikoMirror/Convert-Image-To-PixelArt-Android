
![Ico2n](https://github.com/MikoMIm/Convert-Image-To-PixelArt-Android/assets/102617810/e113b743-d572-4917-80e5-9e6c846a540c)

# Convert-Image-To-PixelArt-Android-App
## Introduction
Android app that allows users to apply a pixelated effect to images using a slider. Users can select an
image from their device, adjust the pixelation level, and save the resulting pixelized image.
## Features
* Simple and intuitive user interface.
* Real-time preview of pixelation effects.
* Save pixelized images to the device's gallery.
* Customizable pixelation levels.
## Pixelization Effect
Below are the steps of how the pixalization process is performed
1. User Interaction: The user interacts with a slider UI element to select the desired level of
pixelation.
2. ViewModel Update: The MainViewModel receives the slider value and updates its internal state
accordingly. It also applies a debounce mechanism to prevent rapid, unnecessary updates to the
pixelization process.
3. Pixelization Task: Once the debounce period has passed, the updatePixelizedBitmap method is
called. This method creates an instance of the Pixelization utility class and uses it to perform the
pixelization process.
4. Pixelization Process: The Pixelization class takes the original bitmap and the slider value to
determine the scale factor for pixelation. It scales down the bitmap to a smaller resolution and
then scales it back up to the original size, creating a pixelated effect. The pixelization process is
performed asynchronously on a background thread (Dispatchers.IO) to avoid blocking the main UI
thread and provide a responsive user experience.
5. Updating LiveData: Once the pixelization is complete, the resulting bitmap is posted to the
_pixelizedBitmap LiveData object. This triggers an observer in the UI layer, which updates the
displayed image to show the pixelated version.
## Examples
<p><b>Example 1</b></p>
<p>Original size: 1128x1382</p>
<p>Density level: 0.71 </p>

![Example2](https://github.com/MikoMIm/Convert-Image-To-PixelArt-Android/assets/102617810/53162835-62fb-451c-b123-282b7c1e73f4)

<hr>
<p><b>Example 2</b></p>
<p>Original size: 1128x1562</p>
<p>Density level: 0.65 </p>

![211](https://github.com/MikoMIm/Convert-Image-To-PixelArt-Android/assets/102617810/b41bc263-0c9a-4f51-8ae8-244bf509f9b2)

<hr>
<p><b>Example 3</b></p>
<p>Original size: 2140x4176</p>
<p>Density level: 1.60 </p>

![Example4](https://github.com/MikoMIm/Convert-Image-To-PixelArt-Android/assets/102617810/d60c22ca-38f7-4d23-8595-5d122c2b7de4)

<hr>

## App Screenshot
<p align="center">
<img width="464" height="991" src="https://github.com/MikoMIm/Convert-Image-To-PixelArt-Android/assets/102617810/b8f9ca46-e2c4-4759-ab70-256bf8cbf75d"> 
</p>








