Drag and Drop Expandable ListView
=================================

Usage.
------

<pre><code>dndListView = (DragNDropListView) findViewById(R.id.list_view_customizer); </code></pre>
<pre><code>dndListView.setDragOnLongPress(true);// set this to enable drag with long press</code></pre>
<pre><code>dndListView.setAdapter(new DragNDropAdapter(this, new int[]{R.layout.row_item}, new int[]{R.id.txt__customizer_item}, children));</code></pre>

