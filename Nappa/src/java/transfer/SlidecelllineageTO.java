/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package transfer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dzuo
 */
public class SlidecelllineageTO implements Serializable {
    private ContainercellTO cell;
    private List<ContainercellTO> pre;
    private List<ContainercellTO> post;

    public SlidecelllineageTO(ContainercellTO cell) {
        this.cell = cell;
        this.pre = new ArrayList<ContainercellTO>();
        this.post = new ArrayList<ContainercellTO>();
    }
    
    public void addPre(ContainercellTO cell) {
        this.pre.add(cell);
    }
    
    public void addPost(ContainercellTO cell) {
        this.post.add(cell);
    }

    public ContainercellTO getCell() {
        return cell;
    }

    public void setCell(ContainercellTO cell) {
        this.cell = cell;
    }
    
    public List<ContainercellTO> getPre() {
        return pre;
    }

    public void setPre(List<ContainercellTO> cells) {
        this.pre = cells;
    }

    public List<ContainercellTO> getPost() {
        return post;
    }

    public void setPost(List<ContainercellTO> post) {
        this.post = post;
    }
}
