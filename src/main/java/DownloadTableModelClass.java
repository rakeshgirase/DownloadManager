import com.exuberant.downloadmanager.service.Download;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.table.AbstractTableModel;

public class DownloadTableModelClass extends AbstractTableModel implements Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String[] columnNames = {"URL","Size","Progress","Status"};
	
	//private static final Class[] columnClasses = {String.class,String.class,JProgressBar.class,String.class};
	
	private List<Download> taskList = new ArrayList<Download>();
	
	public void addDownload(Download download){
		taskList.add(download);
		download.addObserver(this);
		fireTableCellUpdated(getRowCount()-1, getRowCount()-1);
	}
	
	public Download getDownload(int row) {
		return taskList.get(row);
	}
	public int getRowCount() {
		return taskList.size();
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Download download = taskList.get(rowIndex);
		switch(columnIndex){
			case 0:
				return download.getUrl();
			case 1:
				return download.getSize();
			case 2:
				return download.getDownloaded();
			case 3:
				return download.getStatus();
			default:
				return "";
		}
	}

	public void update(Observable o, Object arg) {
		
		
	}

}
