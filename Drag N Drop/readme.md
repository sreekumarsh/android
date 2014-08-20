Drag and Drop Expandable ListView
=================================
Reorder the list items by drag and drop, two ways to drag an item; either set a drag pointer, when a user clicks on that the item gets detached from the list and it can be placed anywhere in the list on any group. Enable drag on long press and then user can long press on any item and the item gets detached from the list.


Usage.
------
```sh
dndListView = (DragNDropListView) findViewById(R.id.list_view_customizer); 
dndListView.setDragOnLongPress(true);// set this to enable drag with long press
dndListView.setAdapter(new DragNDropAdapter(this, new int[]{R.layout.row_item}, new int[]{R.id.txt__customizer_item}, children));
```

Check the MainActivity.java for details.
