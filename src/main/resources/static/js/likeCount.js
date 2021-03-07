var ids = [];

for (var i in likeMarkingList) {
    ids.push(likeMarkingList[i].board_id);
}

window.onload = function() {
    for (var i = 0; i < ids.length; i++) {
        if (document.getElementById(ids[i]) != null) {
            document.getElementById(ids[i]).style.color = "red";
        }
    }
}