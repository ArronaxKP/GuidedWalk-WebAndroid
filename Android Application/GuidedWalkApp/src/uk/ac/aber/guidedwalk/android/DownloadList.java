package uk.ac.aber.guidedwalk.android;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import uk.ac.aber.guidedwalk.model.LoadXML;
import uk.ac.aber.guidedwalk.model.Walk;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

/**
 * This Class presents a list of available walks to the user where the user can
 * decide which walk to download.
 * 
 * @author Karl Parry (kdp8)
 * 
 */
public class DownloadList extends ListActivity {

	private ProgressDialog m_ProgressDialog = null;
	private ArrayList<Walk> walk_list = null;
	private DownloadWalkAdapter m_adapter;
	private Runnable viewWalks;
	private DownloadList context = this;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.downloadlist);
		walk_list = new ArrayList<Walk>();
		this.m_adapter = new DownloadWalkAdapter(this,
				R.layout.downloadwalkitem, walk_list);
		this.setListAdapter(this.m_adapter);
		viewWalks = new Runnable() {
			public void run() {
				getWalkList();
			}
		};
		m_ProgressDialog = ProgressDialog.show(this, "Please wait...",
				"Retrieving data ...", true);
		Thread thread = new Thread(null, viewWalks, "MagentoBackground");
		thread.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView,
	 * android.view.View, int, long)
	 */
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
	}

	/**
	 * This method gets the list of walks from the local copy of the XML Map
	 * file. Once this has been read it runs another thread to redraw the list
	 * to show the list of walks.
	 */
	private void getWalkList() {
		LoadXML lxml = new LoadXML();
		ArrayList<Walk> walk_excludes = lxml.readXMLMap(this, "map.xml");
		if (lxml.downloadXML(this, "map.xml", null, true)) {
			ArrayList<Walk> walk_all = lxml.readXMLMap(this, "temp.xml");
			if (walk_excludes != null) {
				for (int i = 0; i < walk_all.size(); i++) {
					Boolean no_match = true;
					for (int j = 0; j < walk_excludes.size(); j++) {
						if (walk_all.get(i).getId()
								.equals(walk_excludes.get(j).getId())) {
							no_match = false;
							walk_excludes.remove(j);
							j = walk_excludes.size();
						}
					}
					if (no_match) {
						walk_list.add(walk_all.get(i));
					}
				}
			} else {
				for (Walk walk : walk_all){
					walk_list.add(walk);
				}
			}

		} else {
			Log.e("NOMS_MISMATCH", "Number of walks != arraylist.size");
		}
		runOnUiThread(returnRes);
	}

	/**
	 * This variable contains a Runnable Thread that is run once it is called.
	 * It is called once the getWalks method completes. This method passes the
	 * walklist information onto the list adapter Class.
	 */
	private Runnable returnRes = new Runnable() {
		public void run() {
			if (walk_list != null && walk_list.size() > 0) {
				m_adapter.notifyDataSetChanged();
				Button downloadButton = (Button) findViewById(R.id.download_button);
				downloadButton.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						viewWalks = new Runnable() {
							public void run() {
								downloadSelectedWalks();
							}
						};
						m_ProgressDialog = ProgressDialog.show(context,
								"Please wait...", "Retrieving data ...", true);
						Thread thread = new Thread(null, viewWalks,
								"MagentoBackground");
						thread.start();
					}
				});
				m_ProgressDialog.dismiss();
				m_adapter.notifyDataSetChanged();
			} else {
				m_ProgressDialog.dismiss();
				m_adapter.notifyDataSetChanged();
				new AlertDialog.Builder(context)
						.setMessage("There are no new walks to update.")
						.setTitle("Notification")
						.setCancelable(true)
						.setNeutralButton(android.R.string.ok,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int whichButton) {
										context.finish();
									}
								}).show();
			}
		}
	};

	/**
	 * This downloads the walks that have been selected and creates a new local
	 * maps.xml file.
	 */
	public void downloadSelectedWalks() {
		try {
			Document dom = null;
			File file = new File(context.getFilesDir() + "map.xml");
			if (file.exists()) {
				InputStream in = new FileInputStream(file);
				DocumentBuilderFactory dbf = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				dom = db.parse(in, null);
				in.close();
				NodeList nl = dom.getElementsByTagName("numberofwalks");
				Element numberofwalks = (Element) nl.item(0);
				int nows = Integer.parseInt(numberofwalks.getFirstChild()
						.getNodeValue());

				nl = dom.getElementsByTagName("walklist");
				Element walklist = (Element) nl.item(0);
				for (Walk walk : walk_list) {
					if (walk.isSelected()) {
						LoadXML lxml = new LoadXML();
						Boolean success = lxml.downloadXML(context,
								walk.getId() + ".xml", "walks/");
						if (success) {
							Element walkdom = dom.createElement("walk");
							walkdom.setAttribute("id", walk.getId());
							walklist.appendChild(walkdom);

							Element id = dom.createElement("id");
							id.appendChild(dom.createTextNode(walk.getId()));
							walkdom.appendChild(id);

							Element title = dom.createElement("title");
							title.appendChild(dom.createTextNode(walk
									.getWalkTitle()));
							walkdom.appendChild(title);

							Element desc = dom.createElement("desc");
							desc.appendChild(dom.createTextNode(walk
									.getWalkDesc()));
							walkdom.appendChild(desc);

							Element difficulty = dom
									.createElement("difficulty");
							difficulty.appendChild(dom.createTextNode(""
									+ walk.getWalkDifficulty()));
							walkdom.appendChild(difficulty);
							lxml.downloadXML(context, walk.getId(), "walks/");
							nows++;
						}
						walk_list.remove(walk);
					}
					
				}

				numberofwalks.replaceChild(dom.createTextNode("" + nows),
						numberofwalks.getFirstChild());

			} else {
				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder builder;
				builder = factory.newDocumentBuilder();
				dom = builder.newDocument();

				Element rootElement = dom.createElement("walks");
				dom.appendChild(rootElement);

				Element walklist = dom.createElement("walklist");

				int nows = 0;
				for (Walk walk : walk_list) {
					if (walk.isSelected()) {
						LoadXML lxml = new LoadXML();
						Boolean success = lxml.downloadXML(context,
								walk.getId() + ".xml", "walks/");
						if (success) {
							Element walkdom = dom.createElement("walk");
							walkdom.setAttribute("id", walk.getId());
							walklist.appendChild(walkdom);

							Element id = dom.createElement("id");
							id.appendChild(dom.createTextNode(walk.getId()));
							walkdom.appendChild(id);

							Element title = dom.createElement("title");
							title.appendChild(dom.createTextNode(walk
									.getWalkTitle()));
							walkdom.appendChild(title);

							Element desc = dom.createElement("desc");
							desc.appendChild(dom.createTextNode(walk
									.getWalkDesc()));
							walkdom.appendChild(desc);

							Element difficulty = dom
									.createElement("difficulty");
							difficulty.appendChild(dom.createTextNode(""
									+ walk.getWalkDifficulty()));
							walkdom.appendChild(difficulty);
							lxml.downloadXML(context, walk.getId(), "walks/");
							nows++;
						}
					}
				}

				Element now = dom.createElement("numberofwalks");
				rootElement.appendChild(now);
				now.appendChild(dom.createTextNode("" + nows));
				rootElement.appendChild(walklist);
			}
			this.saveNewMap(dom);
			runOnUiThread(returnNew);

		} catch (ParserConfigurationException e) {
			Log.e("CREATE_XML_MAP", e.toString());
			Log.e("CREATE_XML_MAP", Log.getStackTraceString(e));
		} catch (FileNotFoundException e) {
			Log.e("CREATE_XML_MAP", e.toString());
			Log.e("CREATE_XML_MAP", Log.getStackTraceString(e));
		} catch (SAXException e) {
			Log.e("CREATE_XML_MAP", e.toString());
			Log.e("CREATE_XML_MAP", Log.getStackTraceString(e));
		} catch (IOException e) {
			Log.e("CREATE_XML_MAP", e.toString());
			Log.e("CREATE_XML_MAP", Log.getStackTraceString(e));
		}
	}

	/**
	 * This function takes a XML DOM document and writes it to the map.xml file.
	 * 
	 * @param dom
	 *            XML dom document.
	 */
	private void saveNewMap(Document dom) {
		try {
			TransformerFactory transfactory = TransformerFactory.newInstance();
			Transformer transformer = transfactory.newTransformer();
			Properties outFormat = new Properties();
			outFormat.setProperty(OutputKeys.INDENT, "yes");
			outFormat.setProperty(OutputKeys.METHOD, "xml");
			outFormat.setProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			outFormat.setProperty(OutputKeys.VERSION, "1.0");
			outFormat.setProperty(OutputKeys.ENCODING, "UTF-8");
			transformer.setOutputProperties(outFormat);
			DOMSource domSource = new DOMSource(dom.getDocumentElement());
			File file = new File(context.getFilesDir() + "map.xml");
			StreamResult result = new StreamResult(file);
			transformer.transform(domSource, result);
		} catch (TransformerConfigurationException e) {
			Log.e("CREATE_XML_MAP", e.toString());
			Log.e("CREATE_XML_MAP", Log.getStackTraceString(e));
		} catch (TransformerException e) {
			Log.e("CREATE_XML_MAP", e.toString());
			Log.e("CREATE_XML_MAP", Log.getStackTraceString(e));
		}
	}

	private Runnable returnNew = new Runnable() {
		public void run() {
			m_ProgressDialog.dismiss();
			m_adapter.notifyDataSetChanged();
			new AlertDialog.Builder(context)
			.setMessage("Selected Walks Downloaded.")
			.setTitle("Notification")
			.setCancelable(true)
			.setNeutralButton(android.R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							dialog.dismiss();
						}
					}).show();
		}
	};

}