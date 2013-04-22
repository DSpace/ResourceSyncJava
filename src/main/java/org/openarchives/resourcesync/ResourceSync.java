package org.openarchives.resourcesync;

import org.jdom2.Namespace;

import java.text.SimpleDateFormat;

public class ResourceSync
{
    // namespaces
    public static Namespace NS_SITEMAP = Namespace.getNamespace("sm", "http://www.sitemaps.org/schemas/sitemap/0.9");
    public static Namespace NS_RS = Namespace.getNamespace("rs", "http://www.openarchives.org/rs/terms/");
    public static Namespace NS_ATOM = Namespace.getNamespace("atom", "http://www.w3.org/2005/Atom");

    // date format
    public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    // rel values
    public static String REL_DESCRIBED_BY = "describedby";
    public static String REL_DESCRIBES = "describes";
    public static String REL_COLLECTION = "collection";

    // capabilities
    public static String CAPABILITY_RESOURCELIST = "resourcelist";
    public static String CAPABILITY_CHANGELIST = "changelist";
    public static String CAPABILITY_RESOURCEDUMP = "resourcedump";
    public static String CAPABILITY_CHANGEDUMP = "changedump";
    public static String CAPABILITY_RESOURCEDUMP_MANIFEST = "resourcedump-manifest";
    public static String CAPABILITY_CHANGEDUMP_MANIFEST = "changedump-manifest";
    public static String CAPABILITY_CAPABILITYLIST = "capabilitylist";

    // change frequency defined options (although technically you can use others if you want)
    public static String FREQ_ALWAYS = "always";
    public static String FREQ_HOURLY = "hourly";
    public static String FREQ_DAILY = "daily";
    public static String FREQ_WEEKLY = "weekly";
    public static String FREQ_YEARLY = "yearly";
    public static String FREQ_NEVER = "never";

    // change types
    public static String CHANGE_CREATED = "created";
    public static String CHANGE_UPDATED = "updated";
    public static String CHANGE_DELETED = "deleted";

    // supported hashes
    public static String HASH_MD5 = "md5";
    public static String HASH_SHA_256 = "sha-256";
}
