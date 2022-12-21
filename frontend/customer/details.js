const urlToUpdate = 'http://localhost:8080/fibernow/api/v1/customers';
const urlToGetCustomerFromUsername = 'http://localhost:8080/fibernow/api/v1/customers/username/';
const log = (msg) => console.log(msg);
const credentials = 'Basic ' + btoa('admin:admin');
const form = document.getElementById('detail-form');
let username = "ioannispapadopoulos";
let customerObj;

form.addEventListener('submit', editCustomerDetails);

function editCustomerDetails(event) {
    event.preventDefault();
    const payload = new FormData(form);
    const payloadObj = Object.fromEntries(payload);

    payloadObj.phoneNumber = payloadObj.phoneNumber.split(",");
    var columnNames = ['id', 'tin', 'name', 'surname', 'address', 'phoneNumber', 'email', 'username', 'password', 'userStatus', "tickets"];
    for(let i = 0; i < columnNames.length; i++) {
        if(!(columnNames[i] == "tickets")) 
            customerObj[columnNames[i]] = payloadObj[columnNames[i]];
    }
    fetch(urlToUpdate, {
        method: 'POST',
        headers: {
            'Content-Type':
                'application/json;charset=utf-8',
                'Authorization': credentials
        },
        body: JSON.stringify(customerObj)
    })
    .then((response) => response.json())
    .then((response) => response.data)
    .then((data) => {
        console.log(data);
        customerObj = data;
        displayData(data);
    })
    .catch(error => {
        console.log("aaa" + error);
    });
}

window.onload = function loadCustomerDetails() {
    let customer = null;
    let finalUrl = urlToGetCustomerFromUsername + username;
    fetch(finalUrl, {
        method: 'GET',
        headers: {
            'Content-Type':
                'application/json;charset=utf-8',
                'Authorization': credentials
        },
        body: null
    })
    .then((response) => response.json())
    .then((response) => response.data)
    .then(data => {
        customerObj = data;
        displayData(data);
    })
    .catch(error => {
            let errorJson = error.json();
            console.log(error.baseErrors);
            console.log(error);
            alert(error.baseErrors);
            form.reset();
        });
}

function displayData(customer) {
    var columnNames = ['id', 'tin', 'name', 'surname', 'address', 'phoneNumber', 'email', 'username', 'password', 'userStatus', "tickets"];
    for(let i = 0; i < columnNames.length -1; i++) {
        if(columnNames[i] == "ticket") form.elements[i] = null;
        else if(columnNames[i] == "userStatus") form.elements[i].value = customer[columnNames[i]];
        else form.elements[i].defaultValue = customer[columnNames[i]];
    } 
}