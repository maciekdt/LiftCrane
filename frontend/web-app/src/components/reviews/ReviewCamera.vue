<template>
  <div class="camera">
      <video v-show="isCameraOpen" class="camera-video" ref="camera" autoplay playsinline ></video>
  </div>
</template>

<script>
export default {

  name: 'ReviewCamera',

  data() {
    return {
      isCameraOpen: false,
      isPhotoTaken: false
    }
  },


  mounted() {
    this.isCameraOpen = true
    this.createCameraElement()
  },


  methods: {

    createCameraElement () {
      
      const constraints = {
        audio: false,
        video: {
          exact: "environment"
        }
      }

      navigator.mediaDevices
        .getUserMedia(constraints)
        .then(stream => {
          this.$refs.camera.srcObject = stream
        })
        .catch(error => {
          alert(error, "May the browser didn't support or there is some errors.")
        })
    },

    stopCameraStream () {
      const tracks = this.$refs.camera.srcObject.getTracks()
      tracks.forEach(track => {
        track.stop()
      })
      console.log('CameraClosed')
    },
    
    toggleCamera () {
      if (this.isCameraOpen) {
        this.isCameraOpen = false
        this.isPhotoTaken = false
        this.stopCameraStream()
        console.log('closed');
      } else {
        this.isCameraOpen = true
        this.createCameraElement()
        console.log('open');
      }
    },

    takePhoto () {
      this.isPhotoTaken = !this.isPhotoTaken
      const context = this.$refs.canvas.getContext('2d')
      const photoFromVideo = this.$refs.camera
      context.drawImage(photoFromVideo, 0, 0, 450, 337)
    },

    downloadImage() {
    const download = document.getElementById("downloadPhoto");
    const canvas = document.getElementById("photoTaken").toDataURL("image/jpeg")
      .replace("image/jpeg", "image/octet-stream");
    download.setAttribute("href", canvas);
    }

  }
}
</script>

<style>
video{
  height: 100%;
  width: 100%;
}


</style>