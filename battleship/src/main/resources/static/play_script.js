
// Set shot squares/buttons
const shotButtons = document.getElementsByClassName("shot-square");
for (butt of shotButtons) {
	butt.disabled = true;
	if (butt.innerHTML == " ") {
		butt.classList.add('missed-square');
	}
}

const shotSmallSquare = document.getElementsByClassName("shot-small-square");
for (butt of shotSmallSquare) {
	if (butt.innerHTML == " ") {
		butt.classList.add('missed-small-square');
	}
}

// Modals
const aniModal = document.getElementById('animation-modal');
const alertModal = document.getElementById('modal');
const modalButton = document.getElementById('modal-button');

modalButton.addEventListener("click", () => {
	alertModal.close();
});

modalButton.classList.add('submit-button');


// Animation Modal
if (document.querySelector('.show') !== null) {
	var showTime = 5500;
	var sunkNumber = 0;
	var sunkClass = "";

	if (aniModal.classList.contains('sunk')) {
		showTime = 12500;
		sunkNumber = Math.floor(Math.random() * (4 - 1) + 1);
		console.log("Rand: " + sunkNumber);

		if (sunkNumber == 1) { sunkClass = "rock" }
		else if (sunkNumber == 2) { sunkClass = "by-front" }
		else if (sunkNumber == 3) { sunkClass = "by-back" }
	}

	if (!aniModal.classList.contains('ship-none')) {
		console.log("Hit...");
		if (aniModal.classList.contains('carrier')) {
			const carImg = document.getElementById('ani-carrier');
			carImg.classList.add('ship-display');
			if (sunkNumber > 0) {
				carImg.classList.add(sunkClass);
			}
		} else if (aniModal.classList.contains('battleship')) {
			const batImg = document.getElementById('ani-battleship');
			batImg.classList.add('ship-display');
			if (sunkNumber > 0) {
				batImg.classList.add(sunkClass);
			}
		} else if (aniModal.classList.contains('destroyer')) {
			const destImg = document.getElementById('ani-destroyer');
			destImg.classList.add('ship-display');
			if (sunkNumber > 0) {
				destImg.classList.add(sunkClass);
			}
		} else if (aniModal.classList.contains('submarine')) {
			const subImg = document.getElementById('ani-submarine');
			subImg.classList.add('ship-display');
			if (sunkNumber > 0) {
				subImg.classList.add(sunkClass);
			}
		} else if (aniModal.classList.contains('patrolboat')) {
			const patImg = document.getElementById('ani-patrol');
			patImg.classList.add('ship-display');
			if (sunkNumber > 0) {
				patImg.classList.add(sunkClass);
			}
		}
	}

	aniModal.showModal();

	setTimeout(() => {
		aniModal.close();
		alertModal.showModal();
	}, showTime);

} else {
	alertModal.showModal();
}


// Set elements according to whose shot it is
const shot = document.getElementById("shot");

modalButton.addEventListener('click', ()=> {
if (modalButton.classList.contains('player')) {
		const squares = document.getElementsByClassName('square');
		for (sq of squares) {
			sq.disabled = true;
		}
		shot.className = "wait";
		shot.innerHTML = "Waiting for opponent";
		opponentTurn();
} else if (modalButton.classList.contains('opponent')) {
		shot.className = "take-shot";
		shot.innerHTML = "Take your shot !";
} else if (modalButton.classList.contains('winner')) {
		document.getElementById('winner-modal').showModal();
} else if (modalButton.classList.contains('loser')) {
		document.getElementById('loser-modal').showModal();
}
});

//**** If statements should be inside eb=vent listener
//if (modalButton.classList.contains('player')) {
//	modalButton.addEventListener('click', () => {
//		const squares = document.getElementsByClassName('square');
//		for (sq of squares) {
//			sq.disabled = true;
//		}
//		shot.className = "wait";
//		shot.innerHTML = "Waiting for opponent";
//		opponentTurn();
//	})
//} else if (modalButton.classList.contains('opponent')) {
//	modalButton.addEventListener('click', () => {
//		shot.className = "take-shot";
//		shot.innerHTML = "Take your shot !";
//	})
//} else if (modalButton.classList.contains('winner')) {
//	modalButton.addEventListener('click', () => {
//		document.getElementById('winner-modal').showModal();
//	})
//} else if (modalButton.classList.contains('loser')) {
//	modalButton.addEventListener('click', () => {
//		document.getElementById('loser-modal').showModal();
//	})
//}

function opponentTurn() {
	console.log("Waiting......");
	setTimeout(() => {
		document.getElementById('get-form').submit();
	}, 2000);
}


// Show last shot info
const oppLastShot = document.querySelector('.opp-last-shot');
if (oppLastShot.innerHTML === '') {
	document.querySelector('.opp-last-shot-text').style.display = 'none';
}

const lastShot = document.querySelector('.your-last-shot');
if (lastShot.innerHTML === '') {
	document.querySelector('.your-last-shot-text').style.display = 'none';
}


// Mobile buttons
const openButton = document.getElementById('board-open');
const closeButton = document.getElementById('board-close');
const smallBoard = document.querySelector('.grid-3');

openButton.addEventListener('click', () => {
	smallBoard.style.display = 'block';
})

closeButton.addEventListener('click', () => {
	smallBoard.removeAttribute('style');
})

