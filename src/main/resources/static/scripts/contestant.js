document.addEventListener("DOMContentLoaded", function() {

    document.getElementById("logout").addEventListener("click", function() {
        if (!confirm("Bạn có chắc chắn muốn thoát khỏi chương trình thi?")) return;
        document.cookie = "Authorization=";
        localStorage.clear();
        window.location.href = "./";
    });

    document.getElementById("toggle-sidebar").addEventListener("click", function() {
        const sidebar = document.getElementsByClassName("sidebar")[0];
        sidebar.hidden = !sidebar.hidden;
    });

})