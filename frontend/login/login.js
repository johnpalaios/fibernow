loginForm.onsubmit = (event) => {
    event.preventDefault();
    let formData = new FormData(loginForm);
    let requestBodyObject = {
        "username": formData.get("username"),
        "password": formData.get("password")
    };

    let role = "";
    let url = "http://localhost:8080/fibernow/api/v1/login";
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type':
                'application/json;charset=utf-8'
        },
        body: JSON.stringify(requestBodyObject)
    })
        .then((response) => {
            if (response.ok) {
                return response.json();
            }
            throw new Error("Incorrect Username or Password!");
        })
        .then(response => {
            role = response.data;
            let user = {
                "username": "ioannispapadopoulos",
                "password": "password"
            }
            console.log(user);
            if (role === "ADMIN") {
                localStorage.setItem("user",JSON.stringify(requestBodyObject));
                window.location.href = "admin/home.html";
            } else if (role === "CUSTOMER") {
                localStorage.setItem("user",user);
                window.location.href = "customer/tickets.html";
            }
        })
        .catch(error => {
            role = "";
            alert(error.message);
            loginForm.reset();
        });
}