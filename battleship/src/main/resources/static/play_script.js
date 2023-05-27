 	
// Modal
const modalButton = document.getElementById('modal-button');
const alertModal = document.getElementById('modal');
alertModal.showModal();
    
modalButton.addEventListener("click", () => {
	alertModal.close();
	});
	
modalButton.classList.add('submit-button');


const shotButtons = document.getElementsByClassName("shot-square");
for (butt of shotButtons){
	butt.disabled = true;
	if (butt.innerHTML == " "){
		butt.classList.add('missed-square');
	}
}

const shotSmallSquare = document.getElementsByClassName("shot-small-square");
for (butt of shotSmallSquare){
	if (butt.innerHTML == " "){
		butt.classList.add('missed-small-square');
	}
}


const playerButton = document.querySelector('.player');
const opponentButton = document.querySelector('.opponent');
const shot = document.getElementById("shot");

if (playerButton !== null){
    playerButton.addEventListener('click', () => {
		const squares = document.getElementsByClassName('square');
		for (sq of squares) {
			sq.disabled = true;
		}
		shot.className = "wait";
    	shot.innerHTML = "Waiting for opponent";
		opponentTurn();
	})
} else if (opponentButton !== null){
	opponentButton.addEventListener('click', () => {
		shot.className = "take-shot";
    	shot.innerHTML = "Take your shot !"; 
	})
}


function opponentTurn (){
	console.log("Waiting......");
	setTimeout(takeShot, 2000);
	
	function takeShot (){
		document.getElementById('get-form').submit();
	}
}
 	