var errorCnt = 0;
    var id_lst = [];
    var arr = []
    var num = 0;
    var video_state;
    var video_player;

    var genreDict = {};
    var ordered = [];
    var ballardCnt = 0;
    var danceCnt = 0;
    var RBcnt = 0;
    var rockCnt = 0;
    var hiphopCnt = 0;
    var elecCnt = 0;
    var indeCnt = 0;
    var bluesCnt = 0;
    var trotCnt = 0;

    for (var i in data) {
        id_lst.push(data[i].videoId);
    }

    for (var i=0; i<id_lst.length; i++){
        arr[i] = i;
    }


    var totalCnt = id_lst.length;

    flag = 0;
    repeat_flag = 0;
    cnt = 0;

    function shuffle(){
        var j,x,i;
        for (var i = arr.length; i; i -= 1){
            j = Math.floor(Math.random() * i);
            x = arr[i -1];
            arr[i - 1] = arr[j];
            arr[j] = x;
        }
    }


    var tag = document.createElement('script');
    tag.src = "https://www.youtube.com/iframe_api";
    var firstScriptTag = document.getElementsByTagName('script')[0];
    firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);


    var player,
        time_update_interval = 0;


    function onYouTubeIframeAPIReady () {
        player = new YT.Player('playSongIframe', {
                height: '0',
                width: '0',
                loadPlaylist:{
                    listType:'playlist',
                    list: id_lst,
                    index: parseInt(0),
                    suggestedQuality: 'small'
                },
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


    function onPlayerError(event) {
        errorCnt = errorCnt + 1;
        var errorCheck = Number(event.data);

        if (errorCheck == 150 || errorCheck == 100) {
            if (errorCnt == 1) {
                var err_idx = player.getPlaylistIndex();
                id_lst.splice(err_idx,1);
                id_lst.splice(err_idx, 0, data[err_idx].videoId2);
                player.loadPlaylist(id_lst, err_idx, 0, 'large');
            }
            else if (errorCnt == 2){
                var err_idx = player.getPlaylistIndex();
                id_lst.splice(err_idx,1);
                id_lst.splice(err_idx, 0, data[err_idx].videoId3);
                player.loadPlaylist(id_lst, err_idx, 0, 'large');
            }
        }
    }

    function initialize (event) {
        document.getElementById("count").innerHTML = "전체: " + id_lst.length + "곡";
        document.getElementById("link").src = data[0].img;
        document.getElementById("title").innerHTML = data[0].title;
        document.getElementById("artist").innerHTML = data[0].artist;

        var p = event.target;
        p.cuePlaylist(id_lst);
        updateTimerDisplay();
        updateProgressBar();
        clearInterval(time_update_interval);

        time_update_interval = setInterval(function () {
                updateTimerDisplay();
                updateProgressBar();
        }, 1000);


    }

    function getRandomId() {
        return arr.shift();
    }


    function onStateChange(event) {
        if (event.data == YT.PlayerState.PLAYING){
            errorCnt = 0;
        }

        trigger(event.data, event.target);
        play_stop(event.data, event.target);
        video_state = event.data;
        video_player = event.target;
        r_video_state = event.data;
        r_video_player = event.target;

        if (flag == 0) {
            if (repeat_flag == 1) {
                if (event.data == YT.PlayerState.PLAYING) {
                    currentIndex = event.target.getPlaylistIndex();
                }

                if (event.data == YT.PlayerState.ENDED) {
                    if (currentIndex == (id_lst.length -1)) {
                        player.loadPlaylist({
                            'playlist': id_lst,
                            'listType': 'playlist',
                            'index': 0,
                            'startSeconds': 0,
                            'suggestedQuality': 'small'
                        });
                    }
                }
            }
            else if (repeat_flag == 2){
                if (event.data == YT.PlayerState.ENDED){
                    player.previousVideo();
                }
            }
        }
        else {

            if (repeat_flag == 2){
                if (event.data == YT.PlayerState.ENDED){
                    player.previousVideo();
                }
            }
            else {
                if (event.data == YT.PlayerState.ENDED) {
                    num = getRandomId();
                    player.playVideoAt(num);
                    autoshuffle();
                }
            }
        }

        var volume = Math.round(event.target.getVolume());
        var volumeItem = document.getElementById(youTubePlayerVolumeItemId);

        if (volumeItem && (Math.round(volumeItem.value) != volume)) {
            volumeItem.value = volume;
        }
    }

    function autoshuffle() {
        if (!arr.length){
            for (var i=0; i<id_lst.length; i++){
                arr[i] = i;
            }
            shuffle();
        }
    }

    function trigger(state, pl) {
        if (state == YT.PlayerState.PLAYING) {
            index = pl.getPlaylistIndex();
            img_src = data[index].img
            document.getElementById("link").src = img_src;
            document.getElementById("title").innerHTML = data[index].title;
            document.getElementById("artist").innerHTML = data[index].artist;
            document.getElementById(index).innerHTML = "campaign";
            document.getElementsByClassName(index)[0].style.color = "#00CDFF";
            document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[0].style.color = "#00CDFF";
            document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[1].style.color = "#00CDFF";
            document.getElementById(index).scrollIntoView();
        }
        else if (state == YT.PlayerState.ENDED) {
            document.getElementById(index).innerHTML = "";
            document.getElementsByClassName(index)[0].style.color = "black";
        }
    }

    function play_stop(state, pl){
        $(document).ready(function(){
            $('#play').on('click', function() {
                    var play_toggle = $(this);

                    if (state == YT.PlayerState.PLAYING){
                        pl.pauseVideo();
                        play_toggle.text('play_circle_outline');
                    }
                    else {
                        pl.playVideo();
                        play_toggle.text('pause_circle_outline');
                    }
            });
        });
    }

    function youTubePlayerActive() {
        return player && player.hasOwnProperty('getPlayerState');
    }

    function updateTimerDisplay(){
        $('#current-time').text(formatTime( player.getCurrentTime() ));
        $('#duration').text(formatTime(player.getDuration()));
    }

    function initProgressBar(){
        $('#progress-bar').val(0);
    }

    function updateProgressBar(){
        $('#progress-bar').val((player.getCurrentTime() / player.getDuration()) * 100);
    }


    function youTubePlayerVolumeChange(volume) {
        if (youTubePlayerActive()) {
            player.setVolume(volume);
        }
    }


    $(document).ready(function(){
        $('#progress-bar').on('mouseup touchend', function (e) {
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
                mute_toggle.text('volume_off');
            }

        });

        $('#next').on('click', function() {
            if (flag == 1){
                if (arr.length){
                    idx = getRandomId();
                    document.getElementById(index).innerHTML = "";
                    document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[0].style.color = "black";
                    document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[1].style.color = "black";
                    player.playVideoAt(idx);
                }
                else if (!arr.length){
                    for (var i=0; i<id_lst.length; i++){
                        arr[i] = i;
                    }
                    shuffle();
                    idx = getRandomId();
                    document.getElementById(index).innerHTML = "";
                    document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[0].style.color = "black";
                    document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[1].style.color = "black";
                    player.playVideoAt(idx);
                }

            }
            else {
                idx = player.getPlaylistIndex();
                if (idx == id_lst.length -1){
                    idx = -1
                }
                document.getElementById(index).innerHTML = "";
                document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[0].style.color = "black";
                document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[1].style.color = "black";
                player.playVideoAt(idx + 1);
            }

        });

        $('#replay').on('click', function() {
                player.seekTo(0);
        });



        $('#shuffle').on('click', function(e) {
            var shuffle_toggle = $(this);

            if (e.originalEvent){
                if (flag == 0){
                    shuffle();
                    flag = 1;
                    shuffle_toggle.css('color', "black");
                }
                else {
                    flag = 0;
                    shuffle_toggle.css('color', "#a0a0a0");
                }
            }

        });

        $('#repeat').on('click', function(e) {
                var repeat_toggle = $(this);

                if(e.originalEvent) {
                    if (repeat_flag == 0) {
                        repeat_flag = 1;
                        repeat_toggle.text('repeat').css('color', "black");
                    }
                    else if (repeat_flag == 1) {
                        repeat_flag = 2;
                        repeat_toggle.text('repeat_one').css('color', "black");
                    }
                    else if (repeat_flag ==2) {
                        repeat_flag = 0;
                        repeat_toggle.text('repeat').css('color', "#a0a0a0");
                    }
                }

        });

        $('tr').hover(function(){
                $(this).css('background-color',"#D7F1FA");
                $(this).find('button.playbutton').css('background-color',"#D7F1FA");
        }, function(){
                $(this).css('background-color',"white");
                $(this).find('button.playbutton').css('background-color',"white");
        });


        $('#search').keyup(function(){
                var k = $('#search').val()
                $('tr').hide();
                var res = $("tr > td:contains('" + k + "')");
                if (!$(res).text()) {
                    document.getElementById('msg').innerHTML = "검색결과가 없습니다";
                    document.getElementById('word').innerHTML = "'"+ k + "'" ;
                }
                else {
                    $(res).parent().show();
                    document.getElementById('msg').innerHTML = ""
                    document.getElementById('word').innerHTML = ""
                }

        });
    });

    function playYoutube () {
        player.playVideo();
    }


    function pauseYoutube () {
        player.pauseVideo();
    }


    function stopYoutube () {
        player.seekTo(0, true);
        player.stopVideo();
    }

    var played = false;
    function collectPlayCount (data) {
    if (data == YT.PlayerState.PLAYING && played == false) {
        played = true;
        }
    }


    function buttonplay(element) {
        var str = element.value;
        var strarr = str.split(';');

        var title = strarr[0];
        var artist = strarr[1];

        for (var i in data){
            if (data[i].title == title && data[i].artist == artist){
                c_index = i;
            }
        }

        if (arr.indexOf(Number(c_index)) != -1){
            arr.splice(arr.indexOf(Number(c_index)),1);
        }
        player.playVideoAt(c_index);

        document.getElementById('play').innerHTML = 'pause_circle_outline';
        document.getElementById(index).innerHTML = "";
        document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[0].style.color = "black";
        document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[1].style.color = "black";
    }

    function formatTime(time){
        time = Math.round(time);

        var minutes = Math.floor(time / 60),
            seconds = time - minutes * 60;

        seconds = seconds < 10 ? '0' + seconds : seconds;
        return minutes + ":" + seconds;
    }


    document.onkeydown = function(e) {
        if (e.which == 37) {
            player.seekTo(0);
        }
        else if (e.which == 39) {
            if (flag == 1){
                if (arr.length){
                    idx = getRandomId();
                    document.getElementById(index).innerHTML = "";
                    document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[0].style.color = "black";
                    document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[1].style.color = "black";
                    player.playVideoAt(idx);
                }
                else if (!arr.length){
                    for (var i=0; i<id_lst.length; i++){
                        arr[i] = i;
                    }
                    shuffle();
                    idx = getRandomId();
                    document.getElementById(index).innerHTML = "";
                    document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[0].style.color = "black";
                    document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[1].style.color = "black";
                    player.playVideoAt(idx);
                }

            }
            else {
                idx = player.getPlaylistIndex();
                if (idx == id_lst.length -1){
                    idx = -1
                }
                document.getElementById(index).innerHTML = "";
                document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[0].style.color = "black";
                document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[1].style.color = "black";
                player.playVideoAt(idx + 1);
            }
        }
        else if (e.which == 32){
            $(document).ready(function(){
                var play_toggle = $('#play');

                if (video_state == YT.PlayerState.PLAYING){
                    video_player.pauseVideo();
                    play_toggle.text('play_circle_outline');
                }
                else{
                    video_player.playVideo();
                    play_toggle.text('pause_circle_outline');
                }
            });
        }
    }


    var start_pos = 0
    var copy = data.slice();
    var tmp = data.slice();
    var reorder_lst = []

    $(function() {
        $("#sortable").sortable({
            start: function(event, ui){
                start_pos = ui.item.index();
            },
            stop: function(event, ui){
                var update_pos = ui.item.index();

                copy.splice(start_pos, 1);
                copy.splice(update_pos, 0, tmp[start_pos]);

                $(document).ready(function(){
                        $('#reorder').fadeIn('slow');
                });

                var relocatedData = JSON.stringify(copy);
                document.getElementById('relocation').value = relocatedData;
                tmp = copy.slice();
            }
        });
    });


    function hide() {
        $(document).ready(function(){
                $('#reorder').fadeOut('slow');
                window.location.reload();
        });
    }