GoogleMapScripts = {
	init: function() {
		var map = this.createMap();
		var input = this.createPacInput();
		var autocomplete = this.createAutocomplete(map, input);
		var infowindow = new google.maps.InfoWindow();
		var marker = this.createMarker(map);
		
		map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

		google.maps.event.addListener(
			autocomplete,
			'place_changed',
			function() {
				infowindow.close();
				marker.setVisible(false);
				var place = autocomplete.getPlace();
				if (!place.geometry) {
					window.alert("Autocomplete's returned place contains no geometry");
					return;
				}
				if (place.geometry.viewport) {
					map.fitBounds(place.geometry.viewport);
				} else {
					map.setCenter(place.geometry.location);
					map.setZoom(17);
				}
				marker.setIcon(
				({
					url : place.icon,
					size : new google.maps.Size(71, 71),
					origin : new google.maps.Point(0, 0),
					anchor : new google.maps.Point(17, 34),
					scaledSize : new google.maps.Size(35, 35)
				}));
				marker.setPosition(place.geometry.location);
				marker.setVisible(true);

				var address = '';
				if (place.address_components) {
					address = [
						(place.address_components[0] && place.address_components[0].short_name || ''),
						(place.address_components[1] && place.address_components[1].short_name || ''),
						(place.address_components[2] && place.address_components[2].short_name || '')
					].join(' ');
				}

				infowindow.setContent('<div><strong>' + place.name	+ '</strong><br>' + address);
				infowindow.open(map, marker);
				
				$('input#longitude').val(place.geometry.location.lng());
				$('input#latitude').val(place.geometry.location.lat());
			}
		);
		
		$("#tabs").tabs({
	        show: function(e, ui) {
	        	alert(ui.index);
	            if (ui.index == 1) {
	            	alert();
	                google.maps.event.trigger(map, "resize");
	            }
	        }
	    });
		
		$("#map-li").click(function() {
			google.maps.event.trigger(map, "resize");
		});
	},
	
	createMap: function() {
		var mapOptions = {
			center : new google.maps.LatLng(-33.8688, 151.2195),
			zoom : 13
		};
		
		var map = new google.maps.Map($('#map-canvas')[0], mapOptions);
		return map;
	},
	
	createPacInput: function() {
		var input = $('#pac-input')[0];
		$(input).val('');
		return input;
	},
	
	createAutocomplete: function(map, input) {
		var autocomplete = new google.maps.places.Autocomplete(input);
		autocomplete.setTypes([]);
		autocomplete.bindTo('bounds', map);
		return autocomplete
	},
	
	createMarker: function(map) {
		var marker = new google.maps.Marker({
			map : map,
			anchorPoint : new google.maps.Point(0, -29)
		});
		return marker;
	}
}

DatePickerScripts = {
	bindDatePicker: function() {
		$('.formInputIcon').click(function() {
			$('#deadline').datepicker({
		        changeMonth: true,
		        changeYear: true
			});
			$('#deadline').datepicker('show');
		});
	},
	
	bindAll: function() {
		with(DatePickerScripts) {
			bindDatePicker();
		}
	}
}

$(document).ready(function() {
	with (GoogleMapScripts) {
		init();
	}
	with (DatePickerScripts) {
		bindAll();
	}
});