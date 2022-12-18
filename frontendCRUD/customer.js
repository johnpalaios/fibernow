const url = 'http://localhost:8080/fibernow/api/v1/customers';
const log = (msg) => console.log(msg);
const credentials = 'Basic ' + btoa('admin:admin');
const form = document.getElementById('customer-create');
form.addEventListener('submit', createCustomer);

function createCustomer(event) {
    event.preventDefault();
    // const form = document.getElementById('customer-create');
    const payload = new FormData(form);
    // Convert to obj
    const payloadObj = Object.fromEntries(payload);
    payloadObj.phoneNumber = [payloadObj.phoneNumber];
    log(payloadObj);
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8',
            'Authorization': credentials
        },
        body: JSON.stringify(payloadObj),
    })
        .then(response => response.json())
        .then(answer => console.table(answer))
        .then(form.reset())
        .catch(error => console.error('Network Error...', error));
}

function getCustomerById() {
    var customer_id = document.getElementById('find-id').value;
    const urlId = url + "/" + customer_id;

    fetch(urlId, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': credentials
        },
    })
        .then((response) => response.json())
        .then((data) => { console.log(data); })
        .catch((error) => { console.error('Network Error: ', error); });
    document.getElementById('find-id').value = "";
}

function getAllCustomers() {

    fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': credentials
        },
    })
        .then((response) => response.json())
        .then((data) => { console.log(data); })
        .catch((error) => { console.error('Network Error: ', error); });
}

function deleteCustomer() {
    let customer_id = document.getElementById('delete-id').value;
    const urlId = url + "/" + customer_id;

    fetch(urlId, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': credentials
        }
    })
        .then((response) => response.json())
        .then((data) => { console.log(data); })
        .catch((error) => { console.error('Network Error: ', error); });
    document.getElementById('delete-id').value = "";
}