var currentNote;
var maxId = -1;

function changeDisplay(id, action) {
	var element = document.getElementById(id);
	element.style.display = action;
}

function changeElems(id, action) {
	changeDisplay(id, action);

	var iframe = document.getElementById(id).children[0];
	var containerMessage = iframe.contentWindow.document
			.getElementById("message_place");

	var login = iframe.contentWindow.document.getElementById("login");
	var password = iframe.contentWindow.document.getElementById("password");
	var passwordConfirm = iframe.contentWindow.document
			.getElementById('passwordConfirm');

	login.style.border = "3px solid #000";
	password.style.border = "3px solid #000";

	if (passwordConfirm != null) {
		passwordConfirm.style.border = "3px solid #000";
		iframe.style.height = '340px';
		passwordConfirm.value = '';
	} else {
		iframe.style.height = '265px';
	}

	login.value = "";
	password.value = "";

	containerMessage.innerHTML = '';
	containerMessage.style.display = "none";
}

function setCorrectHeightToTextContainer() {
	var text_container = document.getElementById('format-container');
	var screenHeight = screen.height;
	var heightToShow = screen.height * 0.67;
	text_container.style.display = "flex";
	text_container.style.height = heightToShow + "px";
}

function addNote() {

	if (document.getElementById('notes-container').childElementCount == 0) {
		setCorrectHeightToTextContainer();
	}
	maxId += 1;

	addNoteToPage(maxId, 'Title', 'Note');

	let xhr = new XMLHttpRequest();
	let url = "/notes";

	xhr.open("POST", url + "?action=create&maxId=" + maxId, true);
	xhr.send(null);
}

function setText(obj) {
	currentNote = obj;
	document.getElementById('titleText').value = obj.children[0].children[0].children[0].textContent;
	document.getElementById('noteText').value = obj.children[0].children[1].textContent;
}

function saveNote() {
	var title = removeAllTags(document.getElementById('titleText').value);
	var body = removeAllTags(document.getElementById('noteText').value);

	currentNote.children[0].children[0].children[0].textContent = title;
	currentNote.children[0].children[1].textContent = body;

	var noteId = currentNote.id;

	var note = {
		'id' : noteId,
		'title' : document.getElementById('titleText').value,
		'body' : document.getElementById('noteText').value
	};

	var json = JSON.stringify(note);

	let xhr = new XMLHttpRequest();
	let url = "/notes";

	xhr.open("POST", url + "?action=save", true);
	xhr.setRequestHeader("Content-Type", "application/json; charset=utf-8");
	xhr.send(json);

	document.getElementById('notes-container').removeChild(currentNote);

	addNoteToPage(noteId, title, body);
	currentNote = document.getElementById(noteId);
}

function deleteAllNotes() {
	let xhr = new XMLHttpRequest();
	let url = "/notes";

	xhr.open("POST", url + "?action=deleteAllNotes", true);
	xhr.send(null);

	xhr.onreadystatechange = function() {
		if (this.readyState != 4)
			return;

		if (this.status == 200) {
			changeDisplay('format-container', 'none');
			window.location.href = "/notes"
		}
	}
}

function deleteAccount() {
	var oldPassword = document.getElementById('old_passwordForDelete');
	var currentPassword;

	let xhr = new XMLHttpRequest();
	let url = "/profile";

	xhr.open("GET", url + "?action=getPassword", true);
	xhr.responseText = 'text';
	xhr.send(null);

	xhr.onload = function() {
		currentPassword = this.responseText;

		if (oldPassword.value != currentPassword) {
			oldPassword.style.border = "3px solid #F50000";
			addMessage('password_errorForDelete',
					'Current password is not correct', '#F50000');
		} else {
			let xhr = new XMLHttpRequest();
			let url = "/profile";

			xhr.open("POST", url + "?action=deleteUser", true);
			xhr.send(null);

			xhr.onreadystatechange = function() {
				if (this.readyState != 4) {
					return;
				}

				if (this.status == 200) {
					window.location.href = "/main";
				}
			};
		}
	};
}

function deleteNote() {

	var noteId = currentNote.id;

	document.getElementById('notes-container').removeChild(currentNote);
	document.getElementById('titleText').value = "";
	document.getElementById('noteText').value = "";

	if (document.getElementById('notes-container').childElementCount == 0) {
		changeDisplay('format-container', 'none');
	}

	var maxCurrentId = -1;
	var container = document.getElementById('notes-container');

	for (var i = 0; i < container.childElementCount; i++) {
		if (parseInt(container.children[i].id) > maxCurrentId) {
			maxCurrentId = parseInt(container.children[i].id);
		}
	}
	maxId = maxCurrentId;

	let xhr = new XMLHttpRequest();
	let url = "/notes";

	xhr.open("POST", url + "?action=delete&id=" + noteId, true);
	xhr.send(null);
}

function validate() {

	var login = document.getElementById('login');
	var password = document.getElementById('password');

	if (login.value != "") {

		let xhr = new XMLHttpRequest();
		let url = "/login";

		xhr.open("POST", url + "?login=" + login.value + "&password=" + password.value, true);
		xhr.setRequestHeader("Content-Type", "text/text; charset=utf-8");
		xhr.send(null);

		xhr.onreadystatechange = function() {
			if (this.readyState != 4)
				return;

			if (this.status == 201) {
				window.top.location.href = "/main"
			} else if (this.status == 401) {
				if (this.responseText.toLowerCase().includes("password")) {
					login.style.border = "3px solid #000";
					password.style.border = "3px solid #F50000";
				} else if (this.responseText.toLowerCase().includes("login")) {
					login.style.border = "3px solid #F50000";
					password.style.border = "3px solid #000";
				}
				addMessage('message_place', this.responseText, '#F50000');

				iframe = window.top.document.getElementById('log').children[0];
				iframe.style.height = '320px';
			}
		};
	} else {
		login.style.border = "3px solid #F50000";
		iframe = window.top.document.getElementById('log').children[0];
		iframe.style.height = '320px';
		addMessage('message_place', 'Login is empty', '#F50000');
	}
}

function addMessage(id, message, colour) {
	var message_place = document.getElementById(id);
	changeDisplay(id, "block");
	var element = '<a style="display: flex; border: 3px solid'
			+ colour
			+ '; justify-content: center; align-items: center; font-size: 14px; height: 30px; border-radius: 5px;"><b>'
			+ message + '</b></a>';
	message_place.innerHTML = element;
}

function reg() {

	var login = document.getElementById('login');
	var password = document.getElementById('password');
	var passwordConfirm = document.getElementById('passwordConfirm');
	iframe = window.top.document.getElementById('reg').children[0];

	let xhr = new XMLHttpRequest();
	let url = "/reg";

	if (login.value != "") {
		if (password.value.length < 8) {
			login.style.border = "3px solid #000";
			addMessage('message_place', 'Length is less than 8 symbols',
					'#F50000');
			password.style.border = "3px solid #F50000";
			passwordConfirm.style.border = "3px solid #F50000";

			iframe.style.height = '395px';
		} else {
			if (password.value != passwordConfirm.value) {
				login.style.border = "3px solid #000";
				password.style.border = "3px solid #F50000";
				passwordConfirm.style.border = "3px solid #F50000";
				addMessage('message_place', 'Passwords are not the same',
						'#F50000');
				iframe.style.height = '395px';
			} else {
				password.style.border = "3px solid #000";
				passwordConfirm.style.border = "3px solid #000";

				xhr.open("POST", url + "?login=" + login.value + "&password=" + password.value, true);
				xhr.setRequestHeader("Content-Type", "text/text; charset=utf-8");
				xhr.send(null);

				xhr.onreadystatechange = function() {
					if (this.readyState != 4)
						return;

					if (this.status == 201) {
						window.top.location.href = "/main"
					} else if (this.status == 401) {
						login.style.border = "3px solid #F50000";
						addMessage('message_place', 'Login already exist', '#F50000');
						iframe.style.height = '395px';
					}
				};
			}
		}
	} else {
		login.style.border = "3px solid #F50000";
		addMessage('message_place', 'Login is empty', '#F50000');
		iframe.style.height = '395px';
	}
}

function changeLogin() {
	var login = document.getElementById('login');

	if (login.value != "") {

		let xhr = new XMLHttpRequest();
		let url = "/profile";

		xhr.open("POST", url + "?action=changeLogin&login=" + login.value, true);
		xhr.setRequestHeader("Content-Type", "text/text; charset=utf-8");
		xhr.send(null);

		xhr.onreadystatechange = function() {
			if (this.readyState != 4)
				return;

			if (this.status == 200) {
				addMessage('login_error', 'Login succcesfully changed',
						'#00d419');
				login.value = "";
			} else {
				addMessage('login_error', 'Login already exist', '#F50000');
			}
		};
	} else {
		addMessage('login_error', 'Login is empty', '#F50000');
	}
}

function changePassword() {
	var oldPassword = document.getElementById('old_password');
	var newPassword = document.getElementById('new_password');
	var confNewPassword = document.getElementById('conf_new_password');
	var currentPassword;

	let xhr = new XMLHttpRequest();
	let url = "/profile";

	xhr.open("GET", url + "?action=getPassword", true);
	xhr.setRequestHeader("Content-Type", "text/text; charset=utf-8");
	xhr.responseText = 'text';
	xhr.send(null);

	xhr.onload = function() {
		currentPassword = this.responseText;

		if (oldPassword.value != currentPassword) {
			oldPassword.style.border = "3px solid #F50000";
			addMessage('password_error', 'Current password is not correct',
					'#F50000');
		} else {
			oldPassword.style.border = "3px solid #000";
			if (newPassword.value.length < 8) {
				addMessage('password_error', 'Length is less than 8 symbols',
						'#F50000');
				newPassword.style.border = "3px solid #F50000";
				confNewPassword.style.border = "3px solid #F50000";
			} else {
				if (newPassword.value != confNewPassword.value) {
					addMessage('password_error',
							'New passwords are not the same', '#F50000');
					newPassword.style.border = "3px solid #F50000";
					confNewPassword.style.border = "3px solid #F50000";
				} else {
					newPassword.style.border = "3px solid #000";
					confNewPassword.style.border = "3px solid #000";

					let xhr = new XMLHttpRequest();
					let url = "/profile";

					xhr.open("POST", url + "?action=changePassword&password="
							+ newPassword.value, true);
					xhr.send(null);

					xhr.onreadystatechange = function() {
						if (this.readyState != 4) {
							return;
						}

						if (this.status == 200) {
							addMessage('password_error',
									'Password succcesfully changed', '#00d419');
							oldPassword.value = "";
							newPassword.value = "";
							confNewPassword.value = "";
						}
					};
				}
			}
		}
	};
}

function exit() {
	let xhr = new XMLHttpRequest();
	let url = "/profile";

	xhr.open("POST", url + "?action=exit", true);
	xhr.send(null);

	xhr.onreadystatechange = function() {
		if (this.readyState != 4)
			return;

		if (this.status == 200) {
			window.location.href = "/main"
		}
	};
}

function getAllNotes() {

	var JsonNotes;

	let xhr = new XMLHttpRequest();
	let url = "/notes";

	xhr.open("GET", url + "?action=getAll", true);
	xhr.send(null);

	xhr.onreadystatechange = function() {
		if (this.readyState != 4)
			return;

		if (this.status == 200) {
			JsonNotes = this.responseText;
		}
		const Notes = JSON.parse(JsonNotes);

		if (Notes.length != 0) {
			document.getElementById('format-container').style.display = "flex";
			setCorrectHeightToTextContainer();
		}

		showNotes(Notes);
	};
}

function searchByPattern() {
	var JsonNotes;

	var pattern = document.getElementById('q').value;

	if (pattern != "") {

		let xhr = new XMLHttpRequest();
		let url = "/notes?";
		xhr.open("POST", url + "q=" + decodeURIComponent(pattern), true);
		xhr.setRequestHeader("Content-Type", "text/text; charset=utf-8");
		xhr.send(pattern);

		xhr.onreadystatechange = function() {
			if (this.readyState != 4)
				return;

			if (this.status == 200) {
				JsonNotes = this.responseText;
			}
			const Notes = JSON.parse(JsonNotes);

			if (Notes.length != 0) {
				changeDisplay('format-container', 'flex');
				setCorrectHeightToTextContainer();
			} else {
				changeDisplay('format-container', 'none');
			}
			document.getElementById('notes-container').innerHTML = '';
			document.getElementById('titleText').value = "";
			document.getElementById('noteText').value = "";
			showNotes(Notes);
		};
	}
}

function sortByDateCreation() {

	var JsonNotes;

	let xhr = new XMLHttpRequest();
	let url = "/notes";

	xhr.open("POST", url + "?action=sByDateCreation", true);
	xhr.send(null);

	xhr.onreadystatechange = function() {
		if (this.readyState != 4)
			return;

		if (this.status == 200) {
			JsonNotes = this.responseText;
		}
		const Notes = JSON.parse(JsonNotes);

		if (Notes.length != 0) {
			document.getElementById('format-container').style.display = "flex";
			setCorrectHeightToTextContainer();
		}
		document.getElementById('notes-container').innerHTML = '';
		showNotes(Notes);
	};
}

function showNotes(Notes) {

	for (var i = 0; i < Notes.length; i++) {
		addNoteToPage(Notes[i].id, Notes[i].title, Notes[i].body);
		if (parseInt(Notes[i].id) > maxId) {
			maxId = parseInt(Notes[i].id);
		}
	}
}

function checkSendPattern(event) {
	var filterBtns = document.getElementById('q');
	if (event.key == 'Enter' && filterBtns === document.activeElement) {
		searchByPattern();
	}
}

function getWidth() {
	if (self.innerWidth) {
		return self.innerWidth;
	}

	if (document.documentElement && document.documentElement.clientWidth) {
		return document.documentElement.clientWidth;
	}

	if (document.body) {
		return document.body.clientWidth;
	}

	  if (self.innerWidth) {
	    return self.innerWidth;
	  }

	  if (document.documentElement && document.documentElement.clientWidth) {
	    return document.documentElement.clientWidth;
	  }

	  if (document.body) {
	    return document.body.clientWidth;
	  }
	}

function changeStyleForScreen() {
	var filterBtns = document.getElementById('filterBtns');
	if (getWidth() >= 1000) {
		if (filterBtns != null) {
			filterBtns.style.display = "flex";
			filterBtns.style.position = "inherit";
		}
	} else {
		if (filterBtns != null) {
			filterBtns.style.position = "absolute";
		}
	}
}

function showFilterBtns() {
	var filterBtns = document.getElementById('filterBtns');

	if (filterBtns.style.display == "flex") {
		filterBtns.style.display = "none";
		filterBtns.style.position = "inherit";
	} else {
		filterBtns.style.display = "flex";
		filterBtns.style.position = "absolute";
	}
}

function removeAllTags(item) {
	if (item == null || item == "") {
		return "";
	} else {
		item.toString();
	}
	item = item.replace('<', '&lt');
	item = item.replace('>', '&gt');
	return item;
}

function addNoteToPage(id, title, body) {
	title = removeAllTags(title);
	body = removeAllTags(body);
	document
			.getElementById('notes-container')
			.insertAdjacentHTML(
					'afterbegin',
					'<div class="note" id="'
							+ id
							+ '" onclick="setText(this)"> <div style="height: 90%; padding: 10px 20px;"> <p style="font-size: 20px; border-bottom: 2px solid #000; max-height: 46px; padding-bottom: 4px; margin-block-start: 10px;" > <b>'
							+ title + '</b> </p> <p class="body_note">' + body
							+ '</p> </div> </div>');

}