document.addEventListener("DOMContentLoaded", function () {
    document.cookie = "Authorization=";

    const reloadButton = document.getElementById("reload");
    const submitButton = document.getElementById("submit");
    const userField = document.getElementById("user");
    const passwordField = document.getElementById("password");

    function disableAction() {
        reloadButton.disabled = true;
        submitButton.disabled = true;
        userField.disabled = true;
        passwordField.disabled = true;
    }

    function enableAction() {
        reloadButton.disabled = false;
        submitButton.disabled = false;
        userField.disabled = false;
        passwordField.disabled = false;
        reloadButton.textContent = "Làm mới";
        submitButton.textContent = "Đăng nhập";
    }

    function reloadUser() {
        disableAction();
        reloadButton.textContent = "Vui lòng chờ";
        $.ajax({
            url: "/api/accounts",
            method: "GET",
            timeout: 3000,
            success: function (response) {
                userField.innerHTML = "";
                response.forEach(function(element) {
                    let option = document.createElement("option");
                    option.value = element.id;
                    option.innerText = element.name;
                    userField.appendChild(option);
                });
            },
            error: function (jqXHR, textStatus, errorThrown) {
                let error = jqXHR.responseText;
                if (error == undefined || error == "") error = errorThrown;
                toastr.error("Lấy danh sách tài khoản thất bại: " + error);
            }
        }).always(enableAction);
    }

    reloadUser();
    reloadButton.addEventListener("click", reloadUser);

    document.getElementById("login-form").addEventListener("submit", function submit(event) {
        event.preventDefault();
        if (userField.value.length != 36) {
            toastr.warning("Vui lòng kiểm tra lại tài khoản đăng nhập");
            return;
        }
        if (passwordField.value.length != 6) {
            toastr.warning("Vui lòng kiểm tra lại mã đăng nhập");
            return;
        }
        disableAction();
        submitButton.textContent = "Đang kết nối";
        $.ajax({
            url: "/api/login",
            method: "POST",
            contentType: 'application/json',
            timeout: 3000,
            data: JSON.stringify({
                id: userField.value,
                password: passwordField.value
            }),
            success: function(response) {
                document.cookie = `Authorization=${response.token}`;
                toastr.success("Đăng nhập thành công");
                window.location.href = "./";
            },
            error: function (jqXHR, textStatus, errorThrown) {
                let error = jqXHR.responseText;
                if (error == undefined || error == "") error = errorThrown;
                toastr.error("Đăng nhập thất bại: " + error);
            }
        }).always(enableAction);
    });
});
