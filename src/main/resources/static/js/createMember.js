var charSet = [ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
    'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'];
var key = "";
var auth_flag = false;

var flag = false;
var pwcheck = function() {
    if (document.getElementById('password').value == document.getElementById('checkPassword').value) {
        document.getElementById('message').style.color = 'green';
        document.getElementById('message').innerHTML = '비밀번호가 일치합니다';
        flag = true;
    } else {
        document.getElementById('message').style.color = 'red';
        document.getElementById('message').innerHTML = '비밀번호가 일치하지 않습니다.';
        flag = false;
    }

    if (flag == true && auth_flag == true) {
        document.getElementById("registerBtn").disabled = false;
    } else {
        document.getElementById("registerBtn").disabled = "true";
    }

}

var auth = function () {
    key = "";
    var email = document.getElementById("email").value;
    if (email == "") {
        swal("Error", "이메일을 입력해주세요.", "error");
        return 0
    }
    for (let i = 0; i < 10; i++) {
        var idx = Number(Math.floor(Math.random() * charSet.length));
        key += charSet[idx];
    }
    $(document).ready(function () {
        $.ajax({
            url: "/forget/authEmail",
            type: "POST",
            data: {
                "email": email,
                "key": key
            },
        });
    });
    document.getElementById("auth_btn").disabled = "true";
    swal("Success", "입력하신 이메일로 인증번호가 전송되었습니다.", "success");
    auth_flag = true;
    if (auth_flag == true) {
        document.getElementById("registerBtn").disabled = false;
        document.getElementById("auth_input").readOnly = false;
        document.getElementById("exec_btn").disabled = false;
    }
}

var exec = function () {
    var inputKey = document.getElementById("auth_input").value;
    if (key == inputKey) {
        document.getElementById("registerBtn").disabled = false;
        document.getElementById("auth_message").style.color = "green";
        document.getElementById("auth_message").innerHTML = "인증번호가 일치합니다";
    } else {
        document.getElementById("registerBtn").disabled = "true";
        document.getElementById("auth_message").style.color = "red";
        document.getElementById("auth_message").innerHTML = "인증번호가 일치하지 않습니다";
    }

    if (flag == true && auth_flag == true) {
        document.getElementById("registerBtn").disabled = false;
    } else {
        document.getElementById("registerBtn").disabled = "true";
    }
}