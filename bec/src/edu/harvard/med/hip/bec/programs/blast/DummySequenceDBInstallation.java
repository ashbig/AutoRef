/*
 *                    BioJava development code
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  If you do not have a copy,
 * see:
 *
 *      http://www.gnu.org/copyleft/lesser.html
 *
 * Copyright for this code is held jointly by the individual
 * authors.  These should be listed in @author doc comments.
 *
 * For more information on the BioJava project and its aims,
 * or to join the biojava-l mailing list, visit the home page
 * at:
 *
 *      http://www.biojava.org/
 *
 */

package edu.harvard.med.hip.bec.programs.blast;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.biojava.bio.symbol.SymbolList;
import org.biojava.bio.search.SeqSimilaritySearcher;
import org.biojava.bio.search.SeqSimilaritySearchResult;
import org.biojava.bio.search.SequenceDBSearchResult;
import org.biojava.bio.program.sax.BlastLikeSAXParser;
import org.biojava.bio.program.ssbind.BlastLikeSearchBuilder;
import org.biojava.bio.program.ssbind.SeqSimilarityAdapter;
import org.biojava.bio.seq.*;
import org.biojava.bio.*;
import org.biojava.bio.symbol.*;
import org.biojava.bio.seq.db.*;
/**
 * <code>DummySequenceDBInstallation</code> is an implementation which
 * returns the same <code>DummySequenceDB</code> instance regardless
 * of the identifier used to retrieve a database.
 *
 * @author <a href="mailto:kdj@sanger.ac.uk">Keith James</a>
 * @since 1.2
 */
public class DummySequenceDBInstallation implements SequenceDBInstallation
{
    SequenceDB dummyDB;
    Set        sequenceDBs;

    public DummySequenceDBInstallation()
    {
        sequenceDBs = new HashSet();
        dummyDB     = new DummySequenceDB("dummy");
        sequenceDBs.add(dummyDB);
    }

    public SequenceDB getSequenceDB(String identifier)
    {
        return dummyDB;
    }

    public Set getSequenceDBs()
    {
        return Collections.unmodifiableSet(sequenceDBs);
    }
    
    public void addSequenceDB(org.biojava.bio.seq.db.SequenceDB sequenceDB, java.util.Set set)
    {
    }
    
}
