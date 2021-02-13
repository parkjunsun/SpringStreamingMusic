    var errorCnt = 0;

    var current = new Date();
    var future = new Date();
    future.setTime(future.getTime() + 3600000);
    future.setMinutes(0);
    future.setSeconds(0);

    var timeout = (future.getTime() - current.getTime());
    setTimeout(function() { window.location.reload(true); }, timeout);


    let today = new Date();

    let year = today.getFullYear();
    let month = today.getMonth() + 1;
    month = String(month);

    let day = today.getDate();
    day = String(day);

    let hours = today.getHours();
    hours = String(hours);

    if (month.length == 1) {
        month = '0' + month;
    }

    if (day.length == 1) {
        day = '0' + day;
    }

    if (hours.length == 1) {
        hours = '0' + hours;
    }


    window.onload = function() {
         document.getElementById("date").innerHTML = year + '.' + month + '.' + day + " " + hours + ':00'
    }


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


    $(document).ready(function() {

        url1 = "http://localhost:8080/ajax/firstmenu";
        url2 = "http://localhost:8080/ajax/secondmenu";
        url3 = "http://localhost:8080/ajax/thirdmenu";
        url4 = "http://localhost:8080/ajax/fourthmenu";
        url5 = "http://localhost:8080/ajax/fifthmenu";

        $('#val').on('click', function(){
            $.ajax({url: url1, async: false, success:function(result){
                $("#content").hide().html(result).fadeIn('slow');
            }});
        });

        $('#val2').on('click', function(){
            $.ajax({url: url2, async: false, success:function(result){
                $("#content").hide().html(result).fadeIn('slow');
            }});
        });

        $('#val3').on('click', function(){
            $.ajax({url: url3, async: false, success:function(result){
                $("#content").hide().html(result).fadeIn('slow');
            }});
        });

        $('#val4').on('click', function(){
            $.ajax({url: url4, async: false, success:function(result){
                $("#content").hide().html(result).fadeIn('slow');
            }});
        });

        $('#val5').on('click', function(){
            $.ajax({url: url5, async: false, success:function(result){
                $("#content").hide().html(result).fadeIn('slow');
            }});
        });

        $('#progress-bar').on('mouseup touched', function (e) {
            var newTime = player.getDuration() * (e.target.value / 100);
            player.seekTo(newTime);
        });

        $('#mute-toggle').on('click',function() {
            var mute_toggle = $(this);

            if(player.isMuted()){
                player.unMute();
                mute_toggle.text('volume_up');
            }
            else {
                player.mute();
                mute_toggle.text('volume_off')
            }
        });

        $('#replay').on('click', function() {
            player.seekTo(0);
        });

    });


    var tag = document.createElement('script');
    tag.src = "https://www.youtube.com/iframe_api";
    var firstScriptTag = document.getElementsByTagName('script')[0];
    firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);

    var player, time_update_interval = 0;

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


    function initialize(event) {
        var p = event.target;
        player.loadVideoById(videoid, 0, "large");
        document.getElementById("playing_info").innerHTML = top10[index].title +" <i>by</i>&nbsp;&nbsp;" + top10[index].artist;
        document.getElementById("thumb").src = top10[index].img

        updateTimerDisplay();
        updateProgressBar();

        clearInterval(time_update_interval);

        time_update_interval = setInterval(function () {
            updateTimerDisplay();
            updateProgressBar();
        }, 1000);
    }

    function onStateChange(event) {
        if (event.data == YT.PlayerState.PLAYING){
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
                } else {
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
            if (errorCnt == 1) {
                player.loadVideoById(videoid2, 0, "large");
            } else if (errorCnt == 2){
                player.loadVideoById(videoid3, 0, "large");
            }
        }

    }

    function youTubePlayerActive() {
        return player && player.hasOwnProperty('getPlayerState');
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