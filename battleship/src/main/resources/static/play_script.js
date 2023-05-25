 	
const buttons = document.getElementsByClassName("square");
for (butt of buttons){
	butt.disabled = true;
}
const shotButtons = document.getElementsByClassName("shot-square");
for (butt of shotButtons){
	butt.disabled = true;
}
	
setTimeout(takeShot, 1000);

function takeShot() {
    const shot = document.getElementById("shot");
    shot.className = "take-shot";
    shot.innerHTML = "Take your shot !";
  	for (butt of buttons){
   		butt.disabled = false;
   	}
}
 	
// Modal
const modalButton = document.getElementById('modal-button');
const alertModal = document.getElementById('modal');
alertModal.showModal();
/*
	for (butt of buttons){
		butt.addEventListener("click", () => {
        alertModal.showModal();
    });
}*/
// buttons.forEach(butt => {});
    
modalButton.addEventListener("click", () => {
	alertModal.close();
	});