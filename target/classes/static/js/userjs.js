console.log("this is javascript file")

const toggleSidebar = () => {

	if ($(".sidebar").is(":visible")) {
		$(".sidebar").css("display", "none");
		$(".mainContent").css("margin-left", "0%");
	} else {
		//opening sidebar if already close
		$(".sidebar").css("display", "block");
		$(".mainContent").css("margin-left", "20%");
	}
}

function deletContact(cid) {
	swal({
		title: "Are you sure?",
		text: "You want to delete this contact",
		icon: "warning",
		buttons: true,
		dangerMode: true,
	})
		.then((willDelete) => {

			if (willDelete) {
				window.location = "/user/delete/" + cid;
			} else {
				swal("Your Contact is safe!");
			}
		});
}


const search = () => {

	let query = $("#search-input").val();

	if (query == "") {
		console.log("empty search field")
		$(".search-result").hide();
	} else {

		let url = `http://localhost:8080/search/${query}`;

		fetch(url)
		.then(response  =>{
			
			return response.json();
			
		})
		.then(data => {
			console.log(data);
			//console.log(JSON.stringify(data));
			//sending html to webpage to show search result in group items
			
			let result = `<div  class='list-group'>`;
			
			data.forEach(contacts =>{
				
				result+=`<a href='/user/contact-profile/${contacts.cid}' class='list-group-item list-group-item-action'> ${contacts.name} </a>`;
				
			});
				
				result=result+`</div>`;
				$(".search-result").html(result);
				$(".search-result").show();
		});
		

	}

}


const hideform = () =>{
	 
	$(".container").hide();
	$(".registerOTP").show();
	 console.log("hidden form");
}