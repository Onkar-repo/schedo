console.log("Hello Schedo!");

let spList = [];
const spDropdown = document.getElementById("serviceProvider");
const tableList = document.getElementById("reportsTableBody");
async function loadAvailability() {
    try {
		tableList.replaceChildren();
        const payload = {
            ownerEmail: spDropdown.value,
            targetDate: Date.parse(document.getElementById("meetingDateTime").value.split("T")[0])-19800000
        };
		console.log("payload" + payload);
        const mhb = {
            method: 'POST',
            headers: { 'Content-Type': 'Application/json', 'Access-Control-Allow-Origin': '*' },
            body: JSON.stringify(payload)
        };
        const loadResponse = await fetch("/schedo/check", mhb);
		if(!loadResponse.ok){
			console.log(loadResponse.status + ":" + loadResponse.statusText);
			alert(loadResponse.status);
			return;
		}
		const list = await loadResponse.json();
		if(list!==null && list.length > 0 && list[0].message==="ok"){
			console.log(list);
			document.getElementById("errMsg").innerText= "Message: "+"Check existing schedules, working hours and choose appropriate time";
			for(let i =0;i< list.length; i++){
				const tempTr = document.createElement("tr");
				tempTr.className = "hover:bg-slate-50/50 dark:hover:bg-slate-800/30 transition-colors";
				const tempTd1 = document.createElement("td");
				tempTd1.className = "py-3 px-4 w-16 text-center font-medium text-slate-400 dark:text-slate-500";
				tempTd1.innerText = i+1;
				const tempTd2 = document.createElement("td");
				tempTd2.className = "py-3 px-4 font-medium text-slate-900 dark:text-white";
				const tempTd2Div = document.createElement("div");
				tempTd2Div.className = "flex items-center gap-2";
				const tempTd2DivI = document.createElement("i");
				tempTd2DivI.className = "fa-solid fa-handshake text-emerald-600 text-xs";
				const tempTd2DivSpan = document.createElement("span");
				tempTd2DivSpan.innerText = list[i].logVisitorName;
				const tempTd3 = document.createElement("td");
				tempTd3.className = "py-3 px-4 ext-xs";
				const tempTd3Div = document.createElement("div");
				tempTd3Div.className = "text-slate-500";
				const vt = new Date(list[i].logVisitorTime);
				const thisSp = spList.find(item => item.ownerEmail === spDropdown.value);
				if (thisSp === undefined) {
				       alert("Service provider is not selected.");
				       spDropdown.focus();
				       return;
				   }
				
				   console.log(list[i].logVisitorTime);   
				   console.log(thisSp.ownerTimeSpan);   
				const vte= new Date(list[i].logVisitorTime + thisSp.ownerTimeSpan*60000);
				tempTd3Div.innerText = vt.getHours() + ":" + vt.getMinutes() + " To " + vte.getHours() + ":" + vte.getMinutes();
				
				tempTd2Div.appendChild(tempTd2DivI);								
				tempTd2Div.appendChild(tempTd2DivSpan);	
				tempTd3.appendChild(tempTd3Div);						
				tempTd2.appendChild(tempTd2Div);
				tempTr.appendChild(tempTd1);							
				tempTr.appendChild(tempTd2);							
				tempTr.appendChild(tempTd3);							
				tableList.appendChild(tempTr);																	
			}
		}
		else{
			if(list!==null && list.length===0)
			document.getElementById("errMsg").innerText= "Message: "+"No appointments on this day. Entire day is empty. You can book any valid working time.";
			else
	document.getElementById("errMsg").innerText= "Message: "+list[0].message;
		}
    }
    catch (err) {
        console.log(err);
    }
}

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

    const workingHoursSpan = document.getElementById("working-hours");
    const thisSp = spList.find(item => item.ownerEmail === spDropdown.value);

    if (thisSp === undefined) {
        alert("Service provider is not selected.");
        spDropdown.focus();
        return;
    }
    workingHoursSpan.innerText = "Working Time (In 24 Hrs): " + thisSp.ownerStartTime + " to " + thisSp.ownerStartBreak + ", " + thisSp.ownerEndBreak + " to " + thisSp.ownerEndTime;
    document.getElementById("meetingListHeader").innerText = thisSp.ownerName + ": " + document.getElementById("meetingDateTime").value.split("T")[0];
	loadAvailability();
    openModal("reportsModal");
});
