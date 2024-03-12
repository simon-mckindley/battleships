
class ShipCoords {
    constructor(start, end) {
        this.start = start;
        this.end = end;
    }
}

class Square {
    constructor(coords, occupied) {
        this.coords = coords;
        this.occupied = occupied;
    }
}


// Make the element draggable:
dragElement(document.getElementById("Carrier"));
dragElement(document.getElementById("Battleship"));
dragElement(document.getElementById("Destroyer"));
dragElement(document.getElementById("Submarine"));
dragElement(document.getElementById("Patrol-Boat"));

function dragElement(elmnt) {
  var pos1 = 0, pos2 = 0, pos3 = 0, pos4 = 0;
  elmnt.onmousedown = dragMouseDown;
  elmnt.ontouchstart = dragMouseDown;

  function dragMouseDown(e) {
    e = e || window.event;
    e.preventDefault();
    // get the mouse or touch cursor position at startup:
    pos3 = e.clientX || e.touches[0].clientX;
    pos4 = e.clientY || e.touches[0].clientY;
    document.onmouseup = closeDragElement;
    document.ontouchend = closeDragElement;
    // call a function whenever the cursor moves:
    document.onmousemove = elementDrag;
    document.ontouchmove = elementDrag;
  }

  function elementDrag(e) {
    e = e || window.event;
    e.preventDefault();
    // calculate the new cursor position:
    pos1 = pos3 - (e.clientX || e.touches[0].clientX);
    pos2 = pos4 - (e.clientY || e.touches[0].clientY);
    pos3 = e.clientX || e.touches[0].clientX;
    pos4 = e.clientY || e.touches[0].clientY;
    // set the element's new position:
    elmnt.style.top = (elmnt.offsetTop - pos2) + "px";
    elmnt.style.left = (elmnt.offsetLeft - pos1) + "px";
  }

  function closeDragElement() {
    // stop moving when mouse button or touch is released:
    document.onmouseup = null;
    document.onmousemove = null;
    document.ontouchend = null;
    document.ontouchmove = null;
  }
}

/*
function dragElement(elmnt) {
    var pos1 = 0, pos2 = 0, pos3 = 0, pos4 = 0;
    elmnt.onmousedown = dragMouseDown;
    elmnt.on

    function dragMouseDown(e) {
        e = e || window.event;
        e.preventDefault();
        // get the mouse cursor position at startup:
        pos3 = e.clientX;
        pos4 = e.clientY;
        document.onmouseup = closeDragElement;
        // call a function whenever the cursor moves:
        document.onmousemove = elementDrag;
    }

    function elementDrag(e) {
        e = e || window.event;
        e.preventDefault();
        // calculate the new cursor position:
        pos1 = pos3 - e.clientX;
        pos2 = pos4 - e.clientY;
        pos3 = e.clientX;
        pos4 = e.clientY;
        // set the element's new position:
        elmnt.style.top = (elmnt.offsetTop - pos2) + "px";
        elmnt.style.left = (elmnt.offsetLeft - pos1) + "px";
    }

    function closeDragElement() {
        // stop moving when mouse button is released:
        document.onmouseup = null;
        document.onmousemove = null;
    }
}*/


var carrierRot = false;
var battleshipRot = false;
var destroyerRot = false;
var subRot = false;
var patrolRot = false;

function rotateElement(id) {
    var rotate = '(90deg)';
    switch (id) {
        case 'Carrier':
            if (carrierRot) {
                carrierRot = false;
                rotate = '(0deg)';
            } else {
                carrierRot = true;
            }
            break;
        case 'Battleship':
            if (battleshipRot) {
                battleshipRot = false;
                rotate = '(0deg)';
            } else {
                battleshipRot = true;
            }
            break;
        case 'Destroyer':
            if (destroyerRot) {
                destroyerRot = false;
                rotate = '(0deg)';
            } else {
                destroyerRot = true;
            }
            break;
        case 'Submarine':
            if (subRot) {
                subRot = false;
                rotate = '(0deg)';
            } else {
                subRot = true;
            }
            break;
        case 'Patrol-Boat':
            if (patrolRot) {
                patrolRot = false;
                rotate = '(0deg)';
            } else {
                patrolRot = true;
            }
            break;
    }

    document.getElementById(id).style.transform = 'rotate' + rotate;
}


function setShipPositions() {
    const rows = document.getElementsByClassName('row');
    const columns = document.getElementsByClassName('column');
    const ships = document.getElementsByClassName('ship');
    var rowBounds = new Map();
    var colBounds = new Map();
    var shipEndCoords = new Map();
    let allShipsSet = true;
    let unsetShips = new Array;
    let shipCoords = new Map();

    var board = new Array(10);
    for (let i = 0; i < board.length; i++) {
        board[i] = new Array(10);
    }
    for (let row = 0; row < 10; row++) {
        for (let col = 0; col < 10; col++) {
            board[row][col] = new Square((String.fromCharCode(65 + row) + (col + 1)), false);
        }
    }

    for (let i = 0; i < rows.length; i++) {
        let rect = rows[i].getBoundingClientRect();
        rowBounds.set(String.fromCharCode(65 + i), rect);
    }

    for (let i = 0; i < columns.length; i++) {
        let rect = columns[i].getBoundingClientRect();
        colBounds.set(i + 1, rect);
    }

    for (let i = 0; i < ships.length; i++) {
        let rect = ships[i].getBoundingClientRect();
        let id = ships[i].getAttribute('id');
        let coords = setShipEndCoords(rect);
        if (coords !== null) {
            if (checkOverlap(coords)) {
                console.log(id + " - Overlap");
                allShipsSet = false;
                unsetShips.push(id);
            } else {
                shipEndCoords.set(id, coords);
                console.log(id + " S: " + coords.start + " E: " + coords.end);
                shipCoords.set(id, coords);
            }
        } else {
            console.log(id + " - Incorrectly set");
            allShipsSet = false;
            unsetShips.push(id);
        }
    }

    const head = document.querySelector('h2');
    const playButton = document.getElementById('play-button');
    if (allShipsSet) {
		console.log("Carrier: " + shipCoords.get('Carrier').start + '-' + shipCoords.get('Carrier').end);
        document.getElementById('carrier-input').value = shipCoords.get('Carrier').start + '-' + shipCoords.get('Carrier').end;
        document.getElementById('battleship-input').value = shipCoords.get('Battleship').start + '-' + shipCoords.get('Battleship').end;
        document.getElementById('destroyer-input').value = shipCoords.get('Destroyer').start + '-' + shipCoords.get('Destroyer').end;
        document.getElementById('submarine-input').value = shipCoords.get('Submarine').start + '-' + shipCoords.get('Submarine').end;
        document.getElementById('patrol-input').value = shipCoords.get('Patrol-Boat').start + '-' + shipCoords.get('Patrol-Boat').end;
        playButton.style.display = 'block';
        head.innerText = "All ships correctly set";
    } else {
        let errShips = "";
        for (let i = 0; i < unsetShips.length; i++) {
            errShips += unsetShips[i];
            if (i !== unsetShips.length - 1) {
                errShips += ", ";
            }
        }
        playButton.style.display = 'none';
        head.innerText = errShips + " - Incorrectly Set";
    }

    function setShipEndCoords(shipRect) {
        let offset = shipRect.width / 2;
        if (shipRect.height / 2 < offset) {
            offset = shipRect.height / 2;
        }
        let shipTop = shipRect.top + offset;
        let shipBot = shipRect.bottom - offset;
        let shipLeft = shipRect.left + offset;
        let shipRight = shipRect.right - offset;
        let startRow = "";
        let endRow = "";
        let startCol = "";
        let endCol = "";

        for (let [key, value] of rowBounds.entries()) {
            if (shipTop >= value.top && shipTop <= value.bottom) {
                startRow = key;
            }
            if (shipBot >= value.top && shipBot <= value.bottom) {
                endRow = key;
            }
        }
        if (startRow.length === 0 || endRow.length === 0) {
            return null;
        }

        for (let [key, value] of colBounds.entries()) {
            if (shipLeft >= value.left && shipLeft <= value.right) {
                startCol = key;
            }
            if (shipRight >= value.left && shipRight <= value.right) {
                endCol = key;
            }
        }
        if (startCol.length === 0 || endCol.length === 0) {
            return null;
        }

        return new ShipCoords((startRow + startCol), (endRow + endCol));
    }

    function checkOverlap(coords) {
        if (coords.start[0] === coords.end[0]) {
            let row = coords.start.charCodeAt(0) - 65;
            for (let col = parseInt(coords.start.slice(1)) - 1; col < parseInt(coords.end.slice(1)); col++) {
                if (board[row][col].occupied) {
                    return true;
                }
                board[row][col].occupied = true;
                console.log(row + " " + col + ": " + board[row][col].coords + " " + board[row][col].occupied);
            }
        } else {
            let col = parseInt(coords.start.slice(1)) - 1;
            for (let row = coords.start.charCodeAt(0) - 65; row <= coords.end.charCodeAt(0) - 65; row++) {
                if (board[row][col].occupied) {
                    return true;
                }
                board[row][col].occupied = true;
                console.log(row + " " + col + ": " + board[row][col].coords + " " + board[row][col].occupied);
            }
        }

        return false;
    }
}


