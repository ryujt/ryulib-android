package ryulib.dialogs;

import ryulib.listeners.OnNotifyListener;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;

public class OpenDialog {

	public OpenDialog(Context context) {
		_Context = context;
	}
	
	private Context _Context = null;
	private Builder _Dialog = null;
    private OpenDialogLayout _OpenDialogLayout = null;
    
    // Property 
    private String _Path = "/";
    
    // Event
    private OnFileSelectedListener _OnFileSelected = null;
    private OnNotifyListener _OnCanceled = null;
    
    public void show() {
        _OpenDialogLayout = new OpenDialogLayout(_Context);
    	_OpenDialogLayout.setPath(_Path);

        _Dialog = new AlertDialog.Builder(_Context);
        _Dialog.setTitle("Open Dialog");
        _Dialog.setView(_OpenDialogLayout);
        _Dialog.setPositiveButton("Ok", _OnPositiveClick);
        _Dialog.setNegativeButton("Cancel", _OnNegativeClick);

        _Dialog.show();
    }
    
    public String getPath() {    	
    	return _Path;
    }
    
    public void setPath(String path) {
    	_Path = path;
    	if (_OpenDialogLayout != null) _OpenDialogLayout.setPath(_Path);
    }
    
	public void setOnFileSelected(OnFileSelectedListener value) {
		_OnFileSelected = value;
	}

	public OnFileSelectedListener getOnFileSelected() {
		return _OnFileSelected;
	}

	public void setOnCanceled(OnNotifyListener value) {
		_OnCanceled = value;
	}

	public OnNotifyListener getOnCanceled() {
		return _OnCanceled;
	}

	private DialogInterface.OnClickListener _OnPositiveClick = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			if (_OnFileSelected != null) {
				_OnFileSelected.onSelected(_OpenDialogLayout.getPath(), _OpenDialogLayout.getFileName());
			}
		}
	};

    private DialogInterface.OnClickListener _OnNegativeClick = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			if (_OnCanceled != null) {
				_OnCanceled.onNotify(OpenDialog.this);
			}
		}
	};

}
