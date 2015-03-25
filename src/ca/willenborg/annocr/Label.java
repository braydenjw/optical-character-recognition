package ca.willenborg.annocr;

public class Label extends Object {

	private int _name;
	private Label _root;
	private int _rank;
	
	/************************************************************
	 * Constructors
	*************************************************************/
	
	public Label(int name)
	{
	    _name = name;
	    _root = this;
	    _rank = 0;
	}
	
	/************************************************************
	 * Other Methods
	*************************************************************/

	public Label FindRoot()
	{
	    Label self = this;
	    Label root = _root;
	
	    while (self != root) {
	        self = root;
	        root = root.GetRoot();
	    }
	
	    _root = root;
	    return _root;
	}
	
	public void Join(Label secondaryRoot)
	{
	    if (secondaryRoot.GetRank() < _rank) {
		    secondaryRoot.SetRoot(this);
		} else {
		    _root = secondaryRoot;
			if (_rank == secondaryRoot.GetRank()) {
			    secondaryRoot.SetRank(secondaryRoot.GetRank() + 1);
			}
		}
	}
	
	/************************************************************
	 * Override Methods
	*************************************************************/
	
	public boolean equals(Object comparisonObject)
	{
	    Label comparisonLabel = (Label) comparisonObject;
	    return (comparisonLabel != null) && (_name == comparisonLabel.GetName());
	}
	
	public int hashcode()
	{
	    return this.GetName();
	}
	
	public String toString()
	{
	    return ((Integer)this.GetName()).toString();
	}

	/************************************************************
	 * Getters and Setters
	*************************************************************/
	
	public int GetName()
	{
		return _name;
	}
	
	public void SetName(int name)
	{
		_name = name;
	}
	
	public Label GetRoot()
	{
		return _root;
	}
	
	public void SetRoot(Label root)
	{
		_root = root;
	}
	
	public int GetRank()
	{
		return _rank;
	}
	
	public void SetRank(int rank)
	{
		_rank = rank;
	}
	
}
