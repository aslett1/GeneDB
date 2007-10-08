/*
 * Copyright (c) 2007 Genome Research Limited.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Library General Public License as published by the Free
 * Software Foundation; either version 2 of the License or (at your option) any
 * later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Library General Public License
 * along with this program; see the file COPYING.LIB. If not, write to the Free
 * Software Foundation Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307
 * USA
 */

package org.genedb.jogra.plugins;

import org.genedb.jogra.drawing.Jogra;
import org.genedb.jogra.drawing.JograPlugin;
import org.genedb.jogra.drawing.OpenWindowEvent;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventServiceEvent;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ExecutionException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class GeneEditor implements JograPlugin {

    public JFrame getMainPanel(final String title) {

        final JFrame ret = new JFrame();
        ret.setTitle(title);
        final FormLayout fl = new FormLayout("pref 4dlu pref",
                "pref 2dlu pref 2dlu pref 2dlu pref 2dlu pref 2dlu pref 2dlu pref");
        ret.setLayout(fl);
        // JPanel ret = new JPanel(fl);
        // ret.add(new JLabel("Hello"));
        // ret.add(new JLabel("World"));
        final CellConstraints cc = new CellConstraints();

        final JTextField instance = new JTextField("wibble");
        ret.add(new JLabel("Systematic id"), cc.xy(1, 1));
        ret.add(instance, cc.xy(3, 1));

        final JLabel organism = new JLabel("<html><i>Plasmodium falciparum</i> 3D7</html>");
        ret.add(new JLabel("Organism"), cc.xy(1, 3));
        ret.add(organism, cc.xy(3, 3));

        final JTextField username = new JTextField("wobble");
        ret.add(new JLabel("Name"), cc.xy(1, 5));
        ret.add(username, cc.xy(3, 5));

        final JTextField password = new JTextField("hypothetical protein");
        ret.add(new JLabel("Product"), cc.xy(1, 7));
        ret.add(password, cc.xy(3, 7));

        final JTextField blank = new JTextField(" ");
        ret.add(new JLabel("Orthologues"), cc.xy(1, 9));
        ret.add(blank, cc.xy(3, 9));

        ret.add(new JLabel("Paralogues"), cc.xy(1, 11));
        ret.add(blank, cc.xy(3, 11));

        ret.add(new JLabel("Clusters"), cc.xy(1, 13));
        ret.add(new JButton("Show others in cluster"), cc.xy(3, 13));
        ret.pack();
        return ret;
    }

    public JPanel getMainWindowPlugin() {
        final JPanel ret = new JPanel();
        final Box box = Box.createVerticalBox();
        final JLabel label = new JLabel("Gene Editor Search");
        box.add(label);

        final Box row = Box.createHorizontalBox();

        final JTextField query = new JTextField();
        row.add(query);
        final JButton search = new JButton("Search");
        row.add(search);
        search.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent ae) {
                System.err.println("Am I on EDT '" + EventQueue.isDispatchThread() + "' 1");
                new SwingWorker<JFrame, Void>() {

                    @Override
                    protected JFrame doInBackground() throws Exception {
                        return makeWindow(query.getText());
                    }

                    @Override
                    protected void done() {
                        try {
                            final JFrame result = get();
                            final EventServiceEvent e = new OpenWindowEvent(GeneEditor.this, result);
                            EventBus.publish(e);
                        } catch (final InterruptedException exp) {
                            exp.printStackTrace();
                        } catch (final ExecutionException exp) {
                            exp.printStackTrace();
                        }

                    }
                }.execute();
            }
        });
        box.add(row);
        ret.add(box);
        return ret;
    }

    public String getName() {
        return "Gene Editor";
    }

    public int getOrder() {
        return 1;
    }

    public boolean isSingletonByDefault() {
        return false;
    }

    public boolean isUnsaved() {
        // TODO Auto-generated method stub
        return false;
    }

    private JFrame makeWindow(final String search) {
        System.err.println("Am I on EDT '" + EventQueue.isDispatchThread() + "'  x");
        final String title = "Gene: " + search;
        JFrame lookup = Jogra.findNamedWindow(title);
        if (lookup == null) {
            lookup = getMainPanel(title);
        }
        return lookup;
    }

}
