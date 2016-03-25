package pl.arczynskiadam.notesmanager.web.property.editor;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;

import pl.arczynskiadam.notesmanager.web.data.DateFilterData;

public class DateFilterDataEditor extends PropertyEditorSupport {
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		DateFilterData dateFilterData = new DateFilterData();
		
		String from = text.split("\\|")[0].split("\\[")[1];
		String to = text.split("\\|")[1].split("\\]")[0];
		
		dateFilterData.setFrom(LocalDate.of(
				Integer.parseInt(from.split("\\/")[2]),
				Integer.parseInt(from.split("\\/")[1]),
				Integer.parseInt(from.split("\\/")[0])));
		
		dateFilterData.setTo(LocalDate.of(
				Integer.parseInt(to.split("\\/")[2]),
				Integer.parseInt(to.split("\\/")[1]),
				Integer.parseInt(to.split("\\/")[0])));
		
		this.setValue(dateFilterData);
	}

	@Override
	public String getAsText() {
		DateFilterData dateFilterData = (DateFilterData) this.getValue();
		return "[" + dateFilterData.getFrom().toString() + "|" + dateFilterData.getTo().toString() + "]";
	}
}
