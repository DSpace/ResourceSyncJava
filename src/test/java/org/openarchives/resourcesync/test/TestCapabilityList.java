/**
 * The contents of this file are subject to the license and copyright
 * detailed in the LICENSE and NOTICE files at the root of the source
 * tree
 */
package org.openarchives.resourcesync.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openarchives.resourcesync.CapabilityList;
import org.openarchives.resourcesync.ResourceSync;
import org.openarchives.resourcesync.ResourceSyncEntry;
import org.openarchives.resourcesync.ResourceSyncLn;
import org.openarchives.resourcesync.SpecComplianceException;
import org.openarchives.resourcesync.URL;

import java.util.Date;
import java.util.List;

@RunWith(JUnit4.class)
public class TestCapabilityList
{
    @Test
    public void blankConstructor()
    {
        Date now = new Date();
        CapabilityList cl = new CapabilityList();

        assert cl.getCapability().equals(ResourceSync.CAPABILITY_CAPABILITYLIST);
        assert cl.getLastModified() == null;

        List<ResourceSyncLn> lns = cl.getLns();
        assert lns.size() == 0;
    }

    @Test
    public void construction()
    {
        Date now = new Date();
        CapabilityList cl = new CapabilityList("http://describes", now);

        assert cl.getCapability().equals(ResourceSync.CAPABILITY_CAPABILITYLIST);
        assert cl.getLastModified().equals(now);

        List<ResourceSyncLn> lns = cl.getLns();
        assert lns.size() == 1;
    }

    @Test
    public void methods()
    {
        Date now = new Date();
        CapabilityList cl = new CapabilityList();

        cl.addDescribedBy("http://describes1");
        List<ResourceSyncLn> lns = cl.getLns();
        assert lns.size() == 1;

        cl.addDescribedBy("http://describes2");
        lns = cl.getLns();
        assert lns.size() == 2;

        URL url = new URL();
        url.setLoc("http://loc");
        url.setCapability(ResourceSync.CAPABILITY_CHANGELIST);
        try
        {
            cl.addCapableUrl(url);
        }
        catch (SpecComplianceException e)
        {
            assert false;
        }
        List<ResourceSyncEntry> urls = cl.getUrls();
        assert urls.size() == 1;
        assert urls.get(0).getLoc().equals("http://loc");

        try
        {
            cl.addCapableUrl("http://otherloc", ResourceSync.CAPABILITY_RESOURCELIST);
        }
        catch (SpecComplianceException e)
        {
            assert false;
        }
        urls = cl.getUrls();
        assert urls.size() == 2;
        boolean resourcel = false;
        boolean changel = false;
        for (ResourceSyncEntry u : urls)
        {
            if (u.getCapability().equals(ResourceSync.CAPABILITY_RESOURCELIST))
            {
                assert u.getLoc().equals("http://otherloc");
                resourcel = true;
            }
            if (u.getCapability().equals(ResourceSync.CAPABILITY_CHANGELIST))
            {
                assert u.getLoc().equals("http://loc");
                changel = true;
            }
        }
        assert resourcel;
        assert changel;

        try
        {
            cl.addCapableUrl("http://fail", ResourceSync.CAPABILITY_CHANGEDUMP_MANIFEST);
            assert false;
        }
        catch (SpecComplianceException e)
        {
            assert true;
        }


        cl.setResourceList("http://resourcelist");
        cl.setResourceDump("http://resourcedump");
        cl.setChangeList("http://changelist");
        cl.setChangeDump("http://changedump");


        urls = cl.getUrls();
        assert urls.size() == 4;
        boolean resourcelist = false;
        boolean resourcedump = false;
        boolean changelist = false;
        boolean changedump = false;
        for (ResourceSyncEntry u : urls)
        {
            if (u.getCapability().equals(ResourceSync.CAPABILITY_RESOURCELIST))
            {
                assert u.getLoc().equals("http://resourcelist");
                resourcelist = true;
            }
            if (u.getCapability().equals(ResourceSync.CAPABILITY_RESOURCEDUMP))
            {
                assert u.getLoc().equals("http://resourcedump");
                resourcedump = true;
            }
            if (u.getCapability().equals(ResourceSync.CAPABILITY_CHANGELIST))
            {
                assert u.getLoc().equals("http://changelist");
                changelist = true;
            }
            if (u.getCapability().equals(ResourceSync.CAPABILITY_CHANGEDUMP))
            {
                assert u.getLoc().equals("http://changedump");
                changedump = true;
            }
        }
        assert resourcelist;
        assert resourcedump;
        assert changelist;
        assert changedump;
    }

    @Test
    public void manualCheck()
    {
        CapabilityList cl = new CapabilityList();
        cl.addDescribedBy("http://describedby");
        cl.setResourceList("http://resourcelist");
        cl.setResourceDump("http://resourcedump");
        cl.setChangeList("http://changelist");
        cl.setChangeDump("http://changedump");

        System.out.println(cl.serialise());
    }
}
