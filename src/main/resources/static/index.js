console.log("Hello Schedo!");

let spList = [];
const spDropdown = document.getElementById("serviceProvider");

async function loadServiceProviders() {
    try {
        const loadResponse = await fetch("/schedo/load");
        if (!loadResponse.ok) {
            console.log(loadResponse.status + ": " + loadResponse.statusText);
            alert(loadResponse.status);
            return;
        }
        spList = await loadResponse.json();
        if (spList !== null) {
            console.log(spList);
            // const spDropdown = document.getElementById("serviceProvider");
            spDropdown.replaceChildren();
            spDropdown.innerHTML = "<option value=''>Select Service Provider</option>";

            for (let i = 0;i < spList.length;i++) {
                const temp = document.createElement("option");
                temp.value = spList[i].ownerEmail/* + ":" + listOfItems[i].itemName*/;
                temp.textContent = spList[i].ownerId + ":" + spList[i].ownerName + ":" + spList[i].ownerCatagory;
                spDropdown.appendChild(temp);
            }
        }

    }
    catch (err) {
        console.log(err);
    }

}

document.addEventListener('DOMContentLoaded', () => {
    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    const formattedDateTime = `${year}-${month}-${day}T${hours}:${minutes}`;

    document.getElementById("meetingDateTime").value = formattedDateTime;
    loadServiceProviders();
    /*
    To Fetch text of the selected option
    document.getElementById("serviceProvider").options[document.getElementById("serviceProvider").selectedIndex].text;
    */
    document.getElementById("serviceProvider").focus();
});

const checkAvailibity = document.getElementById("check-avail-btn");

checkAvailibity.addEventListener('click', () => {

    const tableList = document.getElementById("reportsTableBody");
    tableList.replaceChildren();
    const workingHoursSpan = document.getElementById("working-hours");
    const thisSp = spList.find(item => item.ownerEmail === spDropdown.value);

    if (thisSp === undefined) {
        alert("Service provider is not selected.");
        spDropdown.focus();
        return;
    }
    workingHoursSpan.innerText = "Working Time (In 24 Hrs): " + thisSp.ownerStartTime + " to " + thisSp.ownerStartBreak + ", " + thisSp.ownerEndBreak + " to " + thisSp.ownerEndTime;
    document.getElementById("meetingListHeader").innerText = thisSp.ownerName + ": " + document.getElementById("meetingDateTime").value.split("T")[0];
    openModal("reportsModal");
});
