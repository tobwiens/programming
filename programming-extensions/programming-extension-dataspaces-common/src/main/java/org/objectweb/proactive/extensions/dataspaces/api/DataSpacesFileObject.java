/*
 * ProActive Parallel Suite(TM):
 * The Open Source library for parallel and distributed
 * Workflows & Scheduling, Orchestration, Cloud Automation
 * and Big Data Analysis on Enterprise Grids & Clouds.
 *
 * Copyright (c) 2007 - 2017 ActiveEon
 * Contact: contact@activeeon.com
 *
 * This library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation: version 3 of
 * the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 */
package org.objectweb.proactive.extensions.dataspaces.api;

/*
 * This interface and some other interfaces in this package are highly inspired by Apache Commons VFS API,
 * package are highly inspired by Apache Commons VFS API,
 */

import java.util.List;

import org.objectweb.proactive.extensions.dataspaces.exceptions.FileSystemException;
import org.objectweb.proactive.extensions.dataspaces.exceptions.SpaceNotFoundException;


/**
 * Instances of this interface represent files within the Data Spaces framework and allows to
 * perform context specific file system operations and file access.
 *
 * A DataSpaceFileObject's File System is backed up by one or more Apache Virtual File Systems.
 * Each Apache VirtualFileSystem points to the same physical file system which can be accessed via
 * several protocols. Some protocols provide stronger performances, other protocols provide encryption,
 * etc.
 * Only one Apache Virtual File System is active at a time, the user can switch to other VFS by using the method
 * switchTo
 * The user can know the list of all available uris by using the method getAllUris
 * <p>
 * Instances of this interface are to be returned by resolve* methods from PADataSpaces
 * class, and therefore refer to its documentation.
 * <p>
 * Some operations may be limited according to the caller's context and granted privileges, see
 * PADataSpaces documentation for the details.
 * <p>
 * All implementors must provide equals method logic. Two DataSpacesFileObject as equal if their URI
 * strings are equal.
 * <p>
 * Instances of this class are not necessarily thread-safe, however it is safe to access the same
 * URI through different DataSpacesFileObject instances (like it happens when accessing the same URI
 * from different Active Objects). It is recommended to explicitly close each instance of this class
 * after usage to make sure that resources are released, preferably in try-finally section.
 */
public interface DataSpacesFileObject {

    /**
     * Returns the file's URI in the ProActive Data Spaces virtual file system. It remains valid when passed
     * to active ActiveObject, and hence can be resolved there trough
     * PADataSpaces#resolveFile(String) method call.
     *
     * @return URI of a represented file without the trailing slash
     */
    String getVirtualURI();

    /**
     * Returns the real file's URI in its current Apache VFS file system.
     * This URI may not be understood by third-party application depending on the provider of the real path.<br />
     * This method returns null if this URI is unknown.
     *
     * @return URI of a represented file without the trailing slash
     */
    String getRealURI();

    /**
     * Returns the base name of this file. The base name is the last element of the file name. For example the base name of /somefolder/somefile is somefile.
     * @return The base name. Never returns null.
     */
    String getBaseName();

    /**
     * Returns the absolute path of this file, within its file system.
     * This path is normalized, so that . and .. elements have been removed.
     * Also, the path only contains / as its separator character.
     * The path always starts with /
     * The root of a file system has / as its absolute path.
     * @return The path. Never returns null.
     */
    String getPath();

    /**
     * In case the dataspace is backed up by several Apache VFS. Returns all URIs referring to this FileObject. If there
     * is only one VFS, then a list containing a single element will be returned.
     * Any of these URI can be used externally or for example via the Apache VFS FileObject API
     *
     * @return List of URI of a represented file
     */
    List<String> getAllRealURIs();

    /**
     * In case the dataspace is backed up by several Apache VFS. Returns all space Root URIs to which this FileObject has access.
     * Any of these URI can be used as parameter to the method switchToSpaceRoot
     *
     * @return List of URI of accessible space roots
     */
    List<String> getAllSpaceRootURIs();

    /**
     * Switches the DataspaceFileObject to the given Root FileSystem. All subsequent calls on this DFO
     * will be done via the new FileSystem. The uri must be a valid uri returned by getAllSpaceRootURIs
     * Example a file://path/to/fs can be switched to ftp://server/path Those two virtual file systems should represent
     * the same real file system, but accessed via different protocols
     *
     * @param uri the new Apache VFS space root
     * @return a new DataSpacesFileObject with the different space root (note that the current DSFO is not modified)
     * @throws IllegalArgumentException if the given uri don't represent a valid uri for this DFO
     */
    DataSpacesFileObject switchToSpaceRoot(String uri) throws FileSystemException, SpaceNotFoundException;

    /**
     * Returns the uri of the current space root
     * @return current space root uri
     */
    String getSpaceRootURI();

    /**
     * Ensures that this FileObject exists and is not write-protected in the remote DataSpace. If the FileObject doesn't exist then the method will
     * try to switch to a different space root, until one is found where the FileObject exists. In practice, this method is
     * preferably used when the current space root is under the protocol "file" (which could be declared as accessible even
     * if the file path doesn't exist or is write protected)
     *
     * @param checkWriteProtected true if this method must check that the FO is not write-protected
     * @return the current DSFO or a new DSFO with a different space root or null if no root was found where this DSFO exists
     * @throws FileSystemException
     * @throws SpaceNotFoundException
     */
    DataSpacesFileObject ensureExistingOrSwitch(boolean checkWriteProtected)
            throws FileSystemException, SpaceNotFoundException;

    /**
     * Determines if this file exists.
     *
     * @return <code>true</code> if this file exists, <code>false</code> if not.
     * @throws FileSystemException
     *             On error determining if this file exists.
     */
    boolean exists() throws FileSystemException;

    /**
     * Determines if this file is hidden.
     *
     * @return <code>true</code> if this file is hidden, <code>false</code> if not.
     * @throws FileSystemException
     *             On error determining if this file exists.
     */
    boolean isHidden() throws FileSystemException;

    /**
     * Determines if this file can be read.
     *
     * @return <code>true</code> if this file is readable, <code>false</code> if not.
     * @throws FileSystemException
     *             On error determining if this file exists.
     */
    boolean isReadable() throws FileSystemException;

    /**
     * Determines if this file can be written to.
     *
     * @return <code>true</code> if this file is writable, <code>false</code> if not.
     * @throws FileSystemException
     *             On error determining if this file exists.
     */
    boolean isWritable() throws FileSystemException;

    /**
     * Returns this file's type.
     *
     * @return One of the {@link FileType} enums. Never returns null.
     * @throws FileSystemException
     *             On error determining the file's type.
     */
    FileType getType() throws FileSystemException;

    /**
     * Determines if this file's data space has a particular capability.
     *
     * @param capability
     *            The capability to check for.
     * @return true if this file's data space has the requested capability.
     */
    boolean hasSpaceCapability(Capability capability);

    /**
     * Returns the folder that contains this file.
     *
     * @return The folder that contains this file. Never returns <code>null</code>.
     * @throws FileSystemException
     *             On error finding the file's parent, e.g. when it does not exist.
     */
    DataSpacesFileObject getParent() throws FileSystemException;

    /**
     * Lists the children of this file.
     *
     * @return An array containing the children of this file. The array is unordered. If the file
     *         does not have any children, a zero-length array is returned. This method never
     *         returns null.
     * @throws FileSystemException
     *             If this file does not exist, or is not a folder, or on error listing this file's
     *             children.
     */
    List<DataSpacesFileObject> getChildren() throws FileSystemException;

    /**
     * Returns a child of this file. Note that this method returns <code>null</code> when the child
     * does not exist.
     *
     * @param name
     *            The name of the child.
     * @return The child, or null if there is no such child.
     * @throws FileSystemException
     *             If this file does not exist, or is not a folder, or on error determining this
     *             file's children.
     */
    DataSpacesFileObject getChild(String name) throws FileSystemException;

    /**
     * Finds a file, relative to this file. Equivalent to calling
     * <code>resolveFile( path, NameScope.FILE_SYSTEM )</code>.
     *
     * @param path
     *            The path of the file to locate. Can either be a relative path or an absolute path.
     * @return The file.
     * @throws FileSystemException
     *             On error parsing the path, or on error finding the file.
     */
    DataSpacesFileObject resolveFile(String path) throws FileSystemException;

    /**
     * Finds the set of matching descendants of this file, in depth-wise order.
     *
     * @param selector
     *            The selector to use to select matching files.
     * @return The matching files. The files are returned in depth-wise order (that is, a child
     *         appears in the list before its parent). Is never <code>null</code> but may represent
     *         an empty list in some cases (e.g. the file does not exist).
     * @throws FileSystemException
     *             when any kind of error occurred while finding files.
     */
    List<DataSpacesFileObject> findFiles(FileSelector selector) throws FileSystemException;

    /**
     * Finds the set of matching descendants of this file, in depth-wise order.
     *
     * @param selector
     *            The selector to use in order to filter files.
     * @return The matching files. The files are returned in depth-wise order (that is, a child
     *         appears in the list before its parent). Is never <code>null</code> but may represent
     *         an empty list in some cases (e.g. the file does not exist).
     * @throws FileSystemException
     *             when any kind of error occurred while finding files.
     */
    List<DataSpacesFileObject> findFiles(org.apache.commons.vfs2.FileSelector selector) throws FileSystemException;

    /**
     * Finds the set of matching descendants of this file.
     *
     * @param selector
     *            the selector used to determine if the file should be selected
     * @param depthwise
     *            controls the ordering in the list. e.g. deepest first
     * @param selected
     *            container for selected files. list needs not to be empty.
     * @throws FileSystemException
     *             when any kind of error occurred while finding files.
     */
    void findFiles(FileSelector selector, boolean depthwise, List<DataSpacesFileObject> selected)
            throws FileSystemException;

    /**
     * Deletes this file. Does nothing if this file does not exist. Does not delete any descendants
     * of this file, use {@link #delete(FileSelector)} for that.
     *
     * @return true if this object has been deleted
     * @throws FileSystemException
     *             If this file is a non-empty folder, or if this file is read-only, or on error
     *             deleting this file.
     */
    boolean delete() throws FileSystemException;

    /**
     * Deletes all descendants of this file that match a selector. Does nothing if this file does
     * not exist.
     * <p>
     * This method is not transactional. If it fails and throws an exception, this file will
     * potentially only be partially deleted.
     *
     * @param selector
     *            The selector to use to select which files to delete.
     * @return the number of deleted objects
     * @throws FileSystemException
     *             If this file or one of its descendants is read-only, or on error deleting this
     *             file or one of its descendants.
     */
    int delete(FileSelector selector) throws FileSystemException;

    /**
     * Creates this folder, if it does not exist. Also creates any ancestor folders which do not
     * exist. This method does nothing if the folder already exists.
     *
     * @throws FileSystemException
     *             If the folder already exists with the wrong type, or the parent folder is
     *             read-only, or on error creating this folder or one of its ancestors.
     */
    void createFolder() throws FileSystemException;

    /**
     * Creates this file, if it does not exist. Also creates any ancestor folders which do not
     * exist. This method does nothing if the file already exists and is a file.
     *
     * @throws FileSystemException
     *             If the file already exists with the wrong type, or the parent folder is
     *             read-only, or on error creating this file or one of its ancestors.
     */
    void createFile() throws FileSystemException;

    /**
     * Copies another file, and all its descendants, to this file.
     * <p>
     * If this file does not exist, it is created. Its parent folder is also created, if necessary.
     * If this file does exist, it is deleted first.
     * <p>
     * This method is not transactional. If it fails and throws an exception, this file will
     * potentially only be partially copied.
     *
     * @param srcFile
     *            The source file to copy.
     * @param selector
     *            The selector to use to select which files to copy.
     * @throws FileSystemException
     *             If this file is read-only, or if the source file does not exist, or on error
     *             copying the file.
     */
    void copyFrom(DataSpacesFileObject srcFile, FileSelector selector) throws FileSystemException;

    /**
     * Move this file.
     * <p>
     * If the destFile exists, it is deleted first.
     *
     * @param destFile
     *            the New filename.
     * @throws FileSystemException
     *             If this file is read-only, or if the source file does not exist, or on error
     *             copying the file.
     */
    void moveTo(DataSpacesFileObject destFile) throws FileSystemException;

    /**
     * Returns this file's content. The {@link FileContent} returned by this method can be used to
     * read and write the content of the file.
     * <p>
     * This method can be called if the file does not exist, and the returned {@link FileContent}
     * can be used to create the file by writing its content.
     *
     * @return This file's content.
     * @throws FileSystemException
     *             On error getting this file's content.
     */
    FileContent getContent() throws FileSystemException;

    /**
     * Closes this file, and its content. This method is a hint to the implementation that it can
     * release any resources associated with the file.
     * <p>
     * The file object can continue to be used after this method is called.
     *
     * @throws FileSystemException
     *             On error closing the file.
     * @see FileContent#close
     */
    void close() throws FileSystemException;

    /**
     * This will prepare the fileObject to get resynchronized with the underlying file system if
     * required
     */
    void refresh() throws FileSystemException;

    /**
     * check if someone reads/write to this file
     */
    boolean isContentOpen();

    /**
     * Checks if this file is a regular file.
     * @return {@code true} if this file is a regular file.
     * @throws FileSystemException if an error occurs.
     */
    boolean isFile() throws FileSystemException;

    /**
     * Checks if this file is a folder.
     * @return {@code true} if this file is a folder.
     * @throws FileSystemException if an error occurs.
     */
    boolean isFolder() throws FileSystemException;

    boolean equals(Object candidate);

    /**
     * Sets the owner's (or everybody's) execute permission.
     *
     * @param readable True to allow execute access, false to disallow.
     * @param ownerOnly If {@code true}, the permission applies only to the owner; otherwise, it applies to everybody.
     * @return {@code true} if the operation succeeded.
     * @throws FileSystemException On error determining if this file exists.
     */
    boolean setExecutable(boolean readable, boolean ownerOnly) throws FileSystemException;

    /**
     * Sets the owner's (or everybody's) read permission.
     *
     * @param readable True to allow read access, false to disallow.
     * @param ownerOnly If {@code true}, the permission applies only to the owner; otherwise, it applies to everybody.
     * @return {@code true} if the operation succeeded.
     * @throws FileSystemException On error determining if this file exists.
     */
    boolean setReadable(boolean readable, boolean ownerOnly) throws FileSystemException;

    /**
     * Sets the owner's (or everybody's) write permission.
     *
     * @param readable True to allow write access, false to disallow.
     * @param ownerOnly If {@code true}, the permission applies only to the owner; otherwise, it applies to everybody.
     * @return {@code true} if the operation succeeded.
     * @throws FileSystemException On error determining if this file exists.
     */
    boolean setWritable(boolean readable, boolean ownerOnly) throws FileSystemException;

}
