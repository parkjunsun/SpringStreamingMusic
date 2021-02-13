var errorCnt = 0;
        let today = new Date();

        let year = today.getFullYear();
        let month = today.getMonth() + 1;
        month = String(month);
        if (month.length == 1){
            month = '0' + month;
        }
        let day = today.getDate();
        day = String(day);
        if (day.length == 1){
            day = '0' + day;
        }
        let hours = today.getHours();
        hours = String(hours);
        if (hours.length == 1){
            hours = '0' + hours;
        }

        window.onload = function() {
            document.getElementById("date").innerHTML = year + '.' + month + '.' + day + " " + hours + ':00'
        }

        $(document).ready(function(){
            $('#allClick').click(function () {
                    if ($("input:checkbox[id='allClick']").prop("checked")){
                        $("input[type=checkbox]").prop("checked", true);
                    } else {
                        $("input[type=checkbox]").prop("checked", false);
                    }
            });

            $('#allClick2').click(function () {
                    if ($("input:checkbox[id='allClick2']").prop("checked")){
                        $("input[type=checkbox]").prop("checked", true);
                    } else {
                        $("input[type=checkbox]").prop("checked", false);
                    }
            });

            $('#progress-bar').on('mouseup touched', function (e) {
                            var newTime = player.getDuration() * (e.target.value / 100);
                            player.seekTo(newTime);
            });

        });

        if (videoIds != null) {
            videoid = videoIds[0]
            videoid2 = videoIds[1]
            videoid3 = videoIds[2]
        } else {
            videoid = null
            videoid2 = null
            videoid3 = null
        }

        if (index != null) {
            index = Number(index);
        } else {
            index = null;
        }

        var tag = document.createElement('script');
        tag.src = "https://www.youtube.com/iframe_api";
        var firstScriptTag = document.getElementsByTagName('script')[0];
        firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);

        var player,
            time_update_interval = 0;

        function onYouTubeIframeAPIReady () {
            player = new YT.Player('playerIframe', {
                height: '0',
                width: '0',
                videoId: '',
                playerVars: {
                    rel: 0
                },
                events: {
                    'onReady': initialize,
                    'onStateChange': onStateChange,
                    'onError': onPlayerError
                }
            });
            var youTubePlayerVolumeItemId = 'YouTube-player-volume';
        }


        function initialize (event) {
            var p = event.target;
            player.loadVideoById(videoid, 0, "large");
            document.getElementById("playing_info").innerHTML = songs[index].title + " <i>by</i>&nbsp;&nbsp;" + songs[index].artist;
            document.getElementById("thumb").src = songs[index].img
            document.getElementsByClassName("rank")[index].scrollIntoView();

            updateTimerDisplay();
            updateProgressBar();

            clearInterval(time_update_interval);

            time_update_interval = setInterval(function () {
                    updateTimerDisplay();
                    updateProgressBar();
            }, 1000);

        }

        function onStateChange(event) {
            if (event.data == YT.PlayerState.PLAYING) {
                errorCnt = 0;
            }

            play_stop(event.data, event.target);

            var volume = Math.round(event.target.getVolume());
            var volumeItem = document.getElementById(youTubePlayerVolumeItemId);

            if (volumeItem && (Math.round(volumeItem.value) != volume)) {
                volumeItem.value = volume;
            }
        }


        function play_stop(state,pl){
            $(document).ready(function(){
                $('#play_toggle').on('click',function() {
                    var play_toggle = $(this);

                    if(state == YT.PlayerState.PLAYING){
                        pl.pauseVideo();
                        play_toggle.text('play_circle_filled');
                    }
                    else{
                        pl.playVideo();
                        play_toggle.text('pause_circle_filled')
                    }
                });
            });
        }


        function onPlayerError(event){
            errorCnt = errorCnt + 1;
            var errorCheck = Number(event.data);

            if (errorCheck == 150 || errorCheck == 100){
                if (errorCnt == 1){
                    player.loadVideoById(videoid2, 0, "large");
                }
                else if (errorCnt == 2){
                    player.loadVideoById(videoid3, 0, "large");
                }
            }
        }


        function updateTimerDisplay() {
            $('#current-time').text(formatTime(player.getCurrentTime()));
            $('#duration').text(formatTime(player.getDuration()));
        }


        function updateProgressBar() {
            $('#progress-bar').val((player.getCurrentTime() / player.getDuration()) * 100);
        }


        function youTubePlayerVolumeChange(volume) {
            if (youTubePlayerActive()) {
                player.setVolume(volume);
            }
        }

        function formatTime(time) {
            time = Math.round(time);

            var minutes = Math.floor(time / 60),
                seconds = time - minutes * 60;

            seconds = seconds < 10 ? '0' + seconds : seconds;
            return minutes + ":" + seconds;
        }