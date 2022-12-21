const url = 'http://localhost:8080/fibernow/api/v1/customers';
const log = (msg) => console.log(msg);
const credentials = 'Basic ' + btoa('admin:admin');
const form = document.getElementById('customer-create');
form.addEventListener('submit', createCustomer);

//// ****************     CREATE CUSTOMER MODAL ******************//
var createModal = document.getElementById("createCustomerModal");
// Get the button that opens the modal
var createBtn = document.getElementById("createCustomerBtn");
// Get the <span> element that closes the modal
var span = document.getElementsByClassName("close11")[0];
// When the user clicks on the button, open the modal
createBtn.onclick = function () {
    createModal.style.display = "block";
}
// When the user clicks on <span> (x), close the modal
span.onclick = function () {
    document.getElementById("createCustomerDetails").innerHTML = "";
    createModal.style.display = "none";
}

// When the user clicks anywhere outside of the modal, close it
window.onclick = function (event) {
    if (event.target == createModal) {
        document.getElementById("createCustomerDetails").innerHTML = "";
        createModal.style.display = "none";
    }
}
//// ****************     END CREATE CUSTOMER MODAL ******************//

function createCustomer(event) {
    log("nere");
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

// function getAllCustomers() {
//     const xhr = new XMLHttpRequest();
//     xhr.open("GET", url);
//     xhr.setRequestHeader('Content-Type', 'application/json');
//     xhr.setRequestHeader('Authorization', credentials);
//     xhr.responseType = 'json';
//     xhr.send();
//     xhr.onload = () => {
//         console.log(xhr.response);
//     }
    
//     xhr.onerror = () => {
//         console.log(xhr);
//     }
// }

function deleteCustomer(customerId) {
    const urlId = url + "/" + customerId;

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
}


function searchCustomers(email, tin) {
    var params = {
        email: email.value,
        tin: tin.value,
    };
    var urlSearch = url + '?';
    var nonEmpty = {};
    Object.keys(params).forEach(key => {
        if (params[key] !== "") {
            nonEmpty[key] = params[key];
        }
    });
    urlSearch += (new URLSearchParams(nonEmpty)).toString();
    fetch(urlSearch, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': credentials
        },
    })
        .then((response) => response.json())
        .then((data) => { displaySearch(data.data); })
        .catch((error) => { console.error('Network Error: ', error); });
}




function displaySearch(data) {
    log(data);
    var ok = document.getElementById("searchCustomersDetails");
    for (const child of ok.children) {
        child.innerHTML = "";
    }

    var columnNames = ['ID', 'TIN', 'Name', 'Surname', 'Address', 'Phone', 'Email', 'Username', 'Password', 'Status'];
    var table = document.getElementById("searchCustomersDetails");
    var tr = document.createElement('tr');
    for (var i = 0; i < columnNames.length; i++) {
        var th = document.createElement('th');
        var text = document.createTextNode(columnNames[i]);
        th.appendChild(text);
        tr.appendChild(th);
    }
    table.appendChild(tr);
    var th = document.createElement('th');
    var text = document.createTextNode('Actions');
    th.appendChild(text);
    tr.appendChild(th);
    table.appendChild(tr);

    for (var j = 0; j < data.length; j++) {
        var tr = document.createElement('tr');
        var dataValues = data[j];
        dataValues = Object.values(dataValues);
        for (var i = 0; i < columnNames.length + 1; i++) {
            if (i < columnNames.length) {
                var td = document.createElement('td');
                td.className = "table-td";
                var input = document.createElement("input");
                input.setAttribute('type', 'text');
                input.id = dataValues[0] + "editCustomerInput";
                input.setAttribute("required", "");
                input.className = "table-input";
                input.value = dataValues[i];
                input.disabled = true;
                td.appendChild(input);
                tr.appendChild(td);
            } else {
                var td = document.createElement('td');
                var editBtn = document.createElement("i");
                var deleteBtn = document.createElement("i");
                editBtn.className = "fa fa-edit btn-edit margin-inline-2";
                editBtn.addEventListener("click", editButton(editBtn));
                deleteBtn.className = "fa fa-trash btn-delete margin-inline-2";
                editBtn.id = dataValues[0] + "editCustomerBtn";
                deleteBtn.id = dataValues[0] + "deleteCustomerBtn";
                deleteBtn.addEventListener("click", deleteButton(deleteBtn));
                td.appendChild(editBtn);
                td.appendChild(deleteBtn);
                tr.appendChild(td);
            }
        }
        table.appendChild(tr);
    }
}

function editButton(editBtn) {
    var count = 0;
    return function () {
        count += 1;
        var id = parseInt(editBtn.id);
        var editInput = id + "editCustomerInput";
        var forms = document.querySelectorAll(`[id="${editInput}"]`);
        if (count % 2 == 1) {
            for (var i = 0; i < forms.length; i++) {
                if (i == 0) continue;
                forms[i].disabled = false;
            }
        }
        if (count % 2 == 0) {
            for (var i = 0; i < forms.length; i++) {
                forms[i].disabled = true;
            }
            updateCustomer(forms);
        }
    }
}

function deleteButton(deleteBtn) {
    var customerId = parseInt(deleteBtn.id);
    return function () {
        deleteCustomer(customerId);
    }
}


function updateCustomer(data) {
    var payload = {};
    log(data);
    payload.id = data[0].value;
    payload.tin = data[1].value;
    payload.name = data[2].value;
    payload.surname = data[3].value;
    payload.address = data[4].value;
    payload.phoneNumber = [data[5].value];
    payload.email = data[6].value;
    payload.username = data[7].value;
    payload.password = data[8].value;
    payload.status = data[9].value;
    log(payload);
    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json;charset=utf-8',
            'Authorization': credentials
        },
        body: JSON.stringify(payload),
    })
        .then(response => response.json())
        .then(response => (response.baseErrors === null) ? alert(`Updated Customer with ID: ${payload.id}`) : alert(response.baseErrors[0].message))
        .then(answer => console.log(answer))
        .catch(error => console.error('Network Error: ', error));
}