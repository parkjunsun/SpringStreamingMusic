var errorCnt = 0;
var id_lst = [];
var arr = []
var num = 0;
var index; // 현재 재생 중인 곡의 인덱스
var video_state;
var video_player;
var youTubePlayerVolumeItemId = 'YouTube-player-volume'; // 볼륨 컨트롤 ID
var lastPlayedIndex; // 마지막으로 재생된 곡의 인덱스

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
var ytCnt = 0;
var etcCnt = 0;


if (lastPlayedSongId) {
    for (var i in data) {
        if (data[i].id == lastPlayedSongId) {
            lastPlayedIndex = i;
            break;
        }
    }
}

// lastPlayedIndex가 설정되지 않은 경우 기본값 0 (첫 곡)
if (typeof lastPlayedIndex === 'undefined' || lastPlayedIndex === null) {
    lastPlayedIndex = 0;
}


for (var i in data) {
    id_lst.push(data[i].videoId);
}

for (var i=0; i<id_lst.length; i++){
    arr[i] = i;
}

var totalCnt = id_lst.length;

for (var i in data){
    if (data[i].genre.includes('발라드')){
        ballardCnt += 1;
    }
    else if (data[i].genre.includes('댄스')){
        danceCnt += 1;
    }
    else if (data[i].genre.includes('R&B')){
        ballardCnt += 1;
    }
    else if (data[i].genre.includes('락')){
        rockCnt += 1;
    }
    else if (data[i].genre.includes('힙합')){
        hiphopCnt += 1;
    }
    else if (data[i].genre.includes('일렉')){
        elecCnt += 1;
    }
    else if (data[i].genre.includes('인디')){
        indeCnt += 1;
    }
    else if (data[i].genre.includes('블루스')){
        ballardCnt += 1;
    }
    else if (data[i].genre.includes('트롯')){
        trotCnt += 1;
    }
    else if (data[i].genre.includes("youtube")) {
        ytCnt += 1;
    }
}

genreAllCnt = ballardCnt + danceCnt + rockCnt + hiphopCnt + elecCnt + indeCnt + trotCnt + ytCnt;
etcCnt = totalCnt - genreAllCnt;

genreDict['발라드'] = ballardCnt / totalCnt * 100;
genreDict['댄스'] = danceCnt / totalCnt * 100;

genreDict['락'] = rockCnt / totalCnt * 100;
genreDict['힙합'] = hiphopCnt / totalCnt * 100;
genreDict['일렉트로니카'] = elecCnt / totalCnt * 100;
genreDict['인디'] = indeCnt / totalCnt * 100;

genreDict['트로트'] = trotCnt / totalCnt * 100;
genreDict['유튜브'] = ytCnt / totalCnt * 100;
genreDict['기타'] = etcCnt / totalCnt * 100;

for (var key in genreDict){
    if (genreDict[key] == 0){
        delete genreDict[key];
    }
}

for (var genre in genreDict){
    ordered.push([genre, genreDict[genre]]);
}


ordered.sort(function(a,b) {
    return b[1] - a[1];
});

window.onload = function() {
    if (id_lst.length == 0) {
        document.getElementsByClassName("progress-bar").style.display = none;
        document.getElementsByClassName("genre_per").style.display = none;
        document.getElementsByClassName("genre_name").style.display = none;
    }
    else {
        var g_index = 0;
        var genre_color = ["#FFA98F", "#CD853F", "#FF5675", "#6495ED", "#4AB34A", "#00A5FF", "#008C8C", "#ff0000", "#2f4f4f"];
        for (var key in ordered){
            var genre_div = document.getElementsByClassName("progress-bar")[g_index];
            var genre_span = document.getElementsByClassName("genre_per")[g_index];
            var genre_name = document.getElementsByClassName("genre_name")[g_index];

            genre_div.style.width = Math.round(ordered[key][1]) + '%';
            genre_div.style.backgroundColor = genre_color[g_index];
            genre_div.innerHTML = Math.round(ordered[key][1]) + '%';
            var genre_type = ordered[key][0];
            if (genre_type == "발라드") {
                genre_div.setAttribute("onclick","location.href=ballardUrl")
            }
            else if (genre_type == "댄스"){
                genre_div.setAttribute("onclick","location.href=danceUrl")
            }
            else if (genre_type == "락"){
                genre_div.setAttribute("onclick","location.href=rockUrl")
            }
            else if (genre_type == "힙합"){
                genre_div.setAttribute("onclick","location.href=hiphopUrl")
            }
            else if (genre_type == "일렉트로니카"){
                genre_div.setAttribute("onclick","location.href=elecUrl")
            }
            else if (genre_type == "인디"){
                genre_div.setAttribute("onclick","location.href=indeUrl")
            }
            else if (genre_type == "트로트"){
                genre_div.setAttribute("onclick","location.href=trotUrl")
            } else if (genre_type == "유튜브"){
                genre_div.setAttribute("onclick","location.href=ytUrl")
            } else if (genre_type == "기타") {
                genre_div.setAttribute("onclick", "location.href=etcUrl")
            }

            genre_span.style.backgroundColor = genre_color[g_index];
            genre_name.innerHTML = ordered[key][0];
            g_index += 1;
        }
    }
}

flag = 0;
repeat_flag = 0;
next_flag = 0;
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
                index: parseInt(lastPlayedIndex),               //변경해야할 부분
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
    document.getElementById("link").src = data[lastPlayedIndex].img;
    document.getElementById("title").innerHTML = data[lastPlayedIndex].title;
    document.getElementById("artist").innerHTML = data[lastPlayedIndex].artist;

    document.getElementById(lastPlayedIndex).innerHTML = "campaign";
    document.getElementsByClassName(lastPlayedIndex)[0].style.color = "#00CDFF";
    document.getElementsByClassName(lastPlayedIndex)[0].getElementsByClassName("shorting")[0].style.color = "#00CDFF";
    document.getElementsByClassName(lastPlayedIndex)[0].getElementsByClassName("shorting")[1].style.color = "#00CDFF";
    document.getElementById(lastPlayedIndex).scrollIntoView({
            behavior: 'smooth',
            block: 'center',
            inline: 'nearest'
    });

    // 첫 곡 인덱스 초기화 - 마지막으로 재생한 곡부터 시작
    num = lastPlayedIndex;

    var p = event.target;
    p.cuePlaylist(id_lst, lastPlayedIndex);
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
                        'index': lastPlayedIndex,
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
                console.log("=== loadVideoById 호출 완료 ===");
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

        // 새 곡의 인덱스 먼저 설정
        var newIndex;
        if (flag == 1 && typeof num !== 'undefined') {
            newIndex = num;
        } else {
            var playlistIndex = pl.getPlaylistIndex();
            if (playlistIndex >= 0) {
                newIndex = playlistIndex;
            }
        }

        // 같은 곡이면 하이라이트 업데이트 건너뛰기 (seekTo, 일시정지 후 재생 등)
        if (prevIndex === newIndex && typeof prevIndex !== 'undefined') {
            console.log("같은 곡 계속 재생 중 - 하이라이트 유지, index:", index);
            return;
        }

        // 이전 곡의 하이라이트 제거 (새 곡으로 전환되는 경우만)
        if (typeof prevIndex !== 'undefined' && prevIndex !== null) {
            document.getElementById(prevIndex).innerHTML = "";
            document.getElementsByClassName(prevIndex)[0].getElementsByClassName("shorting")[0].style.color = "black";
            if (document.getElementsByClassName(prevIndex)[0].getElementsByClassName("shorting")[1].getElementsByTagName("i")[0]) {
                document.getElementsByClassName(prevIndex)[0].getElementsByClassName("shorting")[1].style.color = "red";
            } else {
                document.getElementsByClassName(prevIndex)[0].getElementsByClassName("shorting")[1].style.color = "black";
            }
        }

        // 새 곡 인덱스 최종 설정
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

        $(document).ready(function () {
             //노래시작되면 레코드 기록에 저장 (신규 insert / 기존 횟수+1)
             //마지막으로 들은 곡 Member 테이블에 update
             $.ajax({
                 url: "/record",
                 type: "POST",
                 data: {
                     "id": data[index].id,
                     "title": data[index].title,
                     "artist": data[index].artist,
                     "img": data[index].img,
                     "songid": data[index].songid
                 },
             });
        });
    }
    else if (state == YT.PlayerState.ENDED) {
        console.log("ENDED 이벤트 - index:", index, "flag:", flag, "arr.length:", arr.length);
        // 곡 종료 시에는 하이라이트 제거하지 않음 (다음 곡 재생 시 제거됨)
        next_flag = 0;
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
        next_flag = 1;
        if (flag == 1){
            if (arr.length){
                num = getRandomId();
                document.getElementById(index).innerHTML = "";
                document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[0].style.color = "black";
                if (document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[1].getElementsByTagName("i")[0]) {
                    document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[1].style.color = "red";
                } else {
                    document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[1].style.color = "black";
                }
                player.loadVideoById(id_lst[num]);
            }
            else if (!arr.length){
                for (var i=0; i<id_lst.length; i++){
                    arr[i] = i;
                }
                shuffle();
                num = getRandomId();
                document.getElementById(index).innerHTML = "";
                document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[0].style.color = "black";
                if (document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[1].getElementsByTagName("i")[0]) {
                    document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[1].style.color = "red";
                } else {
                    document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[1].style.color = "black";
                }
                player.loadVideoById(id_lst[num]);
            }

        }
        else {
            idx = player.getPlaylistIndex();
            if (idx == id_lst.length -1){
                idx = -1
            }
            document.getElementById(index).innerHTML = "";
            document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[0].style.color = "black";
            if (document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[1].getElementsByTagName("i")[0]) {
                document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[1].style.color = "red";
            } else {
                document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[1].style.color = "black";
            }
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

                // 플레이리스트 모드 해제를 위해 현재 곡을 loadVideoById로 재로드
                if (typeof index !== 'undefined') {
                    var currentTime = player.getCurrentTime();
                    var wasPlaying = (video_state == YT.PlayerState.PLAYING);
                    num = index;

                    player.loadVideoById({
                        'videoId': id_lst[index],
                        'startSeconds': currentTime
                    });

                    // 재생 중이었으면 자동 재생됨
                    console.log("셔플 모드 전환 - 플레이리스트 모드 해제, 현재 곡:", index, "시간:", currentTime);
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
                player.loadPlaylist({
                    'playlist': id_lst,
                    'listType': 'playlist',
                    'index': index,
                    'startSeconds': currentTime
                });
                console.log("플레이리스트 모드로 복귀, index:", index);
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


    // 셔플 모드일 때는 num 변수 업데이트 및 loadVideoById 사용
    if (flag == 1) {
        num = Number(c_index);
        player.loadVideoById(id_lst[c_index]);
    } else {
        player.playVideoAt(c_index);
    }

    document.getElementById('play').innerHTML = 'pause_circle_outline';
    document.getElementById(index).innerHTML = "";
    document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[0].style.color = "black";
    if (document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[1].getElementsByTagName("i")[0]) {
        document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[1].style.color = "red";
    } else {
        document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[1].style.color = "black";
    }
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
                document.getElementById(index).innerHTML = "";
                document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[0].style.color = "black";
                if (document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[1].getElementsByTagName("i")[0]) {
                    document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[1].style.color = "red";
                } else {
                    document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[1].style.color = "black";
                }
                player.loadVideoById(id_lst[num]);
            }
            else if (!arr.length){
                for (var i=0; i<id_lst.length; i++){
                    arr[i] = i;
                }
                shuffle();
                num = getRandomId();
                document.getElementById(index).innerHTML = "";
                document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[0].style.color = "black";
                if (document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[1].getElementsByTagName("i")[0]) {
                    document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[1].style.color = "red";
                } else {
                    document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[1].style.color = "black";
                }
                player.loadVideoById(id_lst[num]);
            }

        }
        else {
            idx = player.getPlaylistIndex();
            if (idx == id_lst.length -1){
                idx = -1
            }
            document.getElementById(index).innerHTML = "";
            document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[0].style.color = "black";
            if (document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[1].getElementsByTagName("i")[0]) {
                document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[1].style.color = "red";
            } else {
                document.getElementsByClassName(index)[0].getElementsByClassName("shorting")[1].style.color = "black";
            }
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

$(document).ready(function() {
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