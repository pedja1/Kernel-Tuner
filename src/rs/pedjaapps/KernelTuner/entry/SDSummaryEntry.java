package rs.pedjaapps.KernelTuner.entry;

public class SDSummaryEntry {

	private String name;
	private String hrSize;
	private Long size;
	private int percent;
	private int icon;
	
	public SDSummaryEntry(String name, String hrSize, long size, int percent, int icon) {
		super();
		this.name = name;
		this.hrSize = hrSize;
		this.size = size;
		this.percent = percent;
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public String getHrSize() {
		return hrSize;
	}

	public Long getSize() {
		return size;
	}

	public int getPercent() {
		return percent;
	}
	
	public int getIcon() {
		return icon;
	}


}
