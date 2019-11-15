package ryulib.dialogs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OpenDialogLayout extends LinearLayout {

	public OpenDialogLayout(Context context) {
		super(context);
		
		init(context);
	}

	public OpenDialogLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

		init(context);
	}
	
	private void setItemLayout(View view) {
		view.setLayoutParams(
				new LinearLayout.LayoutParams(
			      	ViewGroup.LayoutParams.FILL_PARENT, 
			    	ViewGroup.LayoutParams.WRAP_CONTENT, 
			        0.0F
			    )
			);
	}
	
	private void setListLayout(View view) {
		view.setLayoutParams(
				new LinearLayout.LayoutParams(
			      	ViewGroup.LayoutParams.FILL_PARENT, 
			    	ViewGroup.LayoutParams.FILL_PARENT, 
			        0.0F
			    )
			);
	}
	
	private void init(Context context) {
		setOrientation(LinearLayout.VERTICAL);
		setListLayout(this);
		
		_tvPath = new TextView(context);
		setItemLayout(_tvPath);
		_tvPath.setText("Path: ");
		
		_etFile = new EditText(context);
		setItemLayout(_etFile);
		_etFile.setEnabled(false);
		_etFile.setFocusable(false);
		
        _FileList = new FileList(context);
		setListLayout(_FileList);
        _FileList.setPath("/");
        _FileList.setFocusable(true);
        _FileList.setOnPathChangedListener(_OnPathChanged);
        _FileList.setOnFileSelected(_OnFileSelected);
        
		addView(_tvPath);
		addView(_etFile);
		addView(_FileList);
		
		setFocusable(true);
		setFocusableInTouchMode(true);
	}
	
	private TextView _tvPath = null;
	private EditText _etFile = null;
    private FileList _FileList = null;
    
    public String getPath() {
    	return _FileList.getPath();
    }
    
    public void setPath(String path) {
    	_FileList.setPath(path);
    }
    
    public String getFileName() {
    	return _etFile.getText().toString();
    }
    
    private OnPathChangedListener _OnPathChanged = new OnPathChangedListener() {
		public void onChanged(String path) {
			_tvPath.setText("Path: " + path);
			_etFile.setText("");
		}
	};
	
	private OnFileSelectedListener _OnFileSelected = new OnFileSelectedListener() {
		public void onSelected(String path, String fileName) {
			_etFile.setText(fileName);
		}
	};
	
}
