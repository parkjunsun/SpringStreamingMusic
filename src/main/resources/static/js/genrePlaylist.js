// ============================================
// 전역 변수 선언
// ============================================

// YouTube Player 관련 변수
var errorCnt = 0;                           // 동영상 로드 에러 카운트
    var id_lst = [];                            // YouTube 동영상 ID 리스트
    var arr = []                                // 셔플용 인덱스 배열
    var num = 0;                                // 셔플 모드에서 사용하는 현재 곡 번호
    var index;                                  // 현재 재생 중인 곡의 인덱스
    var video_state;                            // 현재 비디오 상태 (재생/정지 등)
    var video_player;                           // YouTube Player 객체

    // 장르 통계 관련 변수 (사용되지 않음 - 제거 고려)
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

// ============================================
// 플레이리스트 데이터 초기화
// ============================================

    for (var i in data) {
        id_lst.push(data[i].videoId);
    }

    for (var i=0; i<id_lst.length; i++){
        arr[i] = i;
    }


    var totalCnt = id_lst.length;

// ============================================
// 재생 모드 제어 플래그
// ============================================
    flag = 0;           // 0: 일반 재생, 1: 셔플 재생
    repeat_flag = 0;    // 0: 반복 없음, 1: 전체 반복, 2: 한 곡 반복
    cnt = 0;

// ============================================
// 셔플 함수 - 배열을 무작위로 섞음 (Fisher-Yates 알고리즘)
// ============================================

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
            if (event.data === YT.PlayerState.ENDED) {
                console.log("=== 셔플 모드 ENDED 발생 ===");
                console.log("repeat_flag:", repeat_flag);

                if (repeat_flag == 2){
                    // 한곡 반복 모드
                    console.log("한곡 반복 모드 - 이전 곡 재생");
                    player.previousVideo();
                }
                else {
                    // 셔플 재생 모드 (repeat_flag == 0 또는 1)
                    console.log("=== 셔플 랜덤 재생 시작 ===");
                    console.log("현재 index:", index, "flag:", flag, "arr 길이:", arr.length);
                    console.log("ENDED 전 arr:", arr.slice());

                    autoshuffle();
                    console.log("autoshuffle 후 arr:", arr.slice());

                    num = getRandomId();
                    console.log("getRandomId()로 선택된 num:", num);
                    console.log("남은 arr:", arr.slice());
                    console.log("로드할 videoId:", id_lst[num]);

                    // 플레이리스트 자동 진행 방지를 위해 loadVideoById 사용
                    player.loadVideoById(id_lst[num]);
                    console.log("=== loadVideoById 호출 완료, 다음 곡 index:", num, "===");
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
            var prevIndex = index;
            console.log("PLAYING 이벤트 - 이전 index:", index, "flag:", flag, "num:", num);

            // 새 곡의 인덱스 설정
            var newIndex;
            if (flag == 1 && typeof num !== 'undefined') {
                newIndex = num;
                console.log("셔플 모드 - 새 곡 num:", num);
            } else {
                var playlistIndex = pl.getPlaylistIndex();
                if (playlistIndex >= 0) {
                    newIndex = playlistIndex;
                }
            }

            // 같은 곡이면 하이라이트 업데이트 건너뛰기 (일반 모드만)
            if (flag == 0 && prevIndex === newIndex && typeof prevIndex !== 'undefined') {
                console.log("같은 곡 계속 재생 중 - 하이라이트 유지, index:", index);
                return;
            }

            // 이전 곡의 하이라이트 제거
            if (typeof prevIndex !== 'undefined' && prevIndex !== null) {
                resetSongColor(prevIndex);
            }

            // 새 곡 인덱스 설정
            if (flag == 1 && typeof num !== 'undefined') {
                index = num;
                console.log("셔플 모드 - 새 곡 num 사용:", num);
            } else {
                if (newIndex >= 0) {
                    index = newIndex;
                    num = newIndex;
                    console.log("일반 모드 - 새 곡 playlistIndex:", index);

                    // 일반 모드에서만 arr에서 제거
                    var arrIndex = arr.indexOf(Number(index));
                    if (arrIndex !== -1) {
                        arr.splice(arrIndex, 1);
                        console.log("일반 모드 - arr에서", index, "제거, 남은 arr:", arr.slice());
                    }
                }
            }

            // 썸네일 및 정보 업데이트
            img_src = data[index].img
            document.getElementById("link").src = img_src;
            document.getElementById("title").innerHTML = data[index].title;
            document.getElementById("artist").innerHTML = data[index].artist;
            document.getElementById(index).innerHTML = "campaign";
            document.getElementsByClassName(index)[0].style.color = "#00CDFF";
            document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[0].style.color = "#00CDFF";
            document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[1].style.color = "#00CDFF";
            document.getElementById(index).scrollIntoView({
                behavior: 'smooth',
                block: 'center',
                inline: 'nearest'
            });
        }
        else if (state == YT.PlayerState.ENDED) {
            console.log("ENDED 이벤트 - index:", index, "flag:", flag, "arr.length:", arr.length);
            // 곡 종료 시에는 하이라이트 제거하지 않음 (다음 곡 재생 시 제거됨)
        }
    }

    function resetSongColor(idx) {
        if (idx !== undefined && document.getElementById(idx)) {
            document.getElementById(idx).innerHTML = "";
            document.getElementsByClassName(idx)[0].getElementsByClassName("shorting")[0].style.color = "black";
            if (document.getElementsByClassName(idx)[0].getElementsByClassName("shorting")[1].getElementsByTagName("i")[0]) {
                document.getElementsByClassName(idx)[0].getElementsByClassName("shorting")[1].style.color = "red";
            } else {
                document.getElementsByClassName(idx)[0].getElementsByClassName("shorting")[1].style.color = "black";
            }
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
                    num = getRandomId();
                    resetSongColor(index);
                    player.loadVideoById(id_lst[num]);
                }
                else if (!arr.length){
                    for (var i=0; i<id_lst.length; i++){
                        arr[i] = i;
                    }
                    shuffle();
                    num = getRandomId();
                    resetSongColor(index);
                    player.loadVideoById(id_lst[num]);
                }

            }
            else {
                idx = player.getPlaylistIndex();
                if (idx == id_lst.length -1){
                    idx = -1
                }
                resetSongColor(index);
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
                    // 셔플 모드 ON
                    console.log("셔플 ON - 이전 arr:", arr.slice(), "현재 index:", index);

                    // 현재 재생 중인 곡을 arr에서 제거
                    if (typeof index !== 'undefined') {
                        var currentArrIndex = arr.indexOf(Number(index));
                        if (currentArrIndex !== -1) {
                            arr.splice(currentArrIndex, 1);
                            console.log("현재 곡(index:", index, ") arr에서 제거 - 남은 arr:", arr.slice());
                        }
                    }

                    // 나머지 곡들만 섞기
                    shuffle();

                    console.log("셔플 ON - 이후 arr:", arr.slice());
                    flag = 1;
                    shuffle_toggle.css('color', "black");

                    // 플레이리스트 모드 해제를 위해 현재 곡을 재로드
                    if (typeof index !== 'undefined') {
                        var currentTime = player.getCurrentTime();
                        var wasPlaying = (video_state == YT.PlayerState.PLAYING);
                        num = index;

                        // 재생 중이면 loadVideoById, 아니면 cueVideoById로 버퍼링 최소화
                        if (wasPlaying) {
                            player.loadVideoById({
                                'videoId': id_lst[index],
                                'startSeconds': currentTime,
                                'suggestedQuality': 'small'  // 빠른 로딩
                            });
                            console.log("셔플 ON - 재생 중, 빠른 로딩으로 전환");
                        } else {
                            player.cueVideoById({
                                'videoId': id_lst[index],
                                'startSeconds': currentTime
                            });
                            console.log("셔플 ON - 일시정지, 버퍼링 없이 큐잉");
                        }
                    }
                }
                else {
                    // 셔플 모드 OFF: 배열 초기화
                    console.log("셔플 OFF - 배열 초기화");
                    arr = [];
                    for (var i=0; i<id_lst.length; i++){
                        arr[i] = i;
                    }
                    // 현재 재생 중인 곡은 제거
                    if (typeof index !== 'undefined') {
                        var currentArrIndex = arr.indexOf(Number(index));
                        if (currentArrIndex !== -1) {
                            arr.splice(currentArrIndex, 1);
                        }
                    }
                    console.log("셔플 OFF - arr:", arr.slice(), "index:", index);
                    flag = 0;
                    shuffle_toggle.css('color', "#a0a0a0");

                    // 셔플 OFF 시 플레이리스트 모드로 복귀
                    var currentTime = player.getCurrentTime();
                    var wasPlaying = (video_state == YT.PlayerState.PLAYING);

                    if (wasPlaying) {
                        player.loadPlaylist({
                            'playlist': id_lst,
                            'listType': 'playlist',
                            'index': index,
                            'startSeconds': currentTime,
                            'suggestedQuality': 'small'  // 빠른 로딩
                        });
                        console.log("셔플 OFF - 재생 중, 빠른 로딩으로 복귀");
                    } else {
                        player.cuePlaylist({
                            'playlist': id_lst,
                            'listType': 'playlist',
                            'index': index,
                            'startSeconds': currentTime
                        });
                        console.log("셔플 OFF - 일시정지, 버퍼링 없이 큐잉");
                    }
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

        // 이전 하이라이트 제거 (현재 재생 중인 곡 또는 초기 하이라이트)
        if (typeof index !== 'undefined' && index !== null) {
            resetSongColor(index);
        } else {
            // 아직 재생이 시작되지 않았다면 초기 하이라이트(index 0) 제거
            resetSongColor(0);
        }

        // 셔플 모드일 때는 num 변수 업데이트 및 loadVideoById 사용
        if (flag == 1) {
            num = Number(c_index);
            player.loadVideoById(id_lst[c_index]);
        } else {
            player.playVideoAt(c_index);
        }

        document.getElementById('play').innerHTML = 'pause_circle_outline';
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
                    num = getRandomId();
                    resetSongColor(index);
                    player.loadVideoById(id_lst[num]);
                }
                else if (!arr.length){
                    for (var i=0; i<id_lst.length; i++){
                        arr[i] = i;
                    }
                    shuffle();
                    num = getRandomId();
                    resetSongColor(index);
                    player.loadVideoById(id_lst[num]);
                }

            }
            else {
                idx = player.getPlaylistIndex();
                if (idx == id_lst.length -1){
                    idx = -1
                }
                resetSongColor(index);
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
                        $('#reorder-overlay').fadeIn('slow');
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
                $('#reorder-overlay').fadeOut('slow');
                $('#reorder').fadeOut('slow');
                window.location.reload();
        });
    }