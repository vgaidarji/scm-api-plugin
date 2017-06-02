package jenkins.scm.api;

import hudson.model.Actionable;
import jenkins.scm.impl.mock.MockSCMController;
import jenkins.scm.impl.mock.MockSCMHead;
import jenkins.scm.impl.mock.MockSCMRevision;
import jenkins.scm.impl.mock.MockSCMSource;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * @author Stephen Connolly
 */
public class SCMRevisionActionTest {
    @Test
    public void given__legacyData__when__gettingRevision__then__legacyReturned() throws Exception {
        MockSCMController c = MockSCMController.create();
        try {
            MockSCMSource source = new MockSCMSource("foo", c, "test", true, false, false);
            SCMRevision revision = new MockSCMRevision(new MockSCMHead("head"), "hash");
            Actionable a = new ActionableImpl();
            a.addAction(new SCMRevisionAction(revision, null));
            assertThat(SCMRevisionAction.getRevision(source, a), is(revision));
        } finally {
            c.close();
        }
    }

    @Test
    public void given__mixedData__when__gettingRevision__then__legacyReturnedForUnmatched() throws Exception {
        MockSCMController c = MockSCMController.create();
        try {
            MockSCMSource source1 = new MockSCMSource("foo", c, "test", true, false, false);
            MockSCMSource source2 = new MockSCMSource("bar", c, "test", true, false, false);
            MockSCMSource source3 = new MockSCMSource("manchu", c, "test", true, false, false);
            SCMRevision revision1 = new MockSCMRevision(new MockSCMHead("head1"), "hash1");
            SCMRevision revision2 = new MockSCMRevision(new MockSCMHead("head2"), "hash2");
            SCMRevision revision3 = new MockSCMRevision(new MockSCMHead("head3"), "hash3");
            Actionable a = new ActionableImpl();
            a.addAction(new SCMRevisionAction(source1, revision1));
            a.addAction(new SCMRevisionAction(revision2, null));
            a.addAction(new SCMRevisionAction(source3, revision3));
            assertThat(SCMRevisionAction.getRevision(source1, a), is(revision1));
            assertThat(SCMRevisionAction.getRevision(source2, a), is(revision2));
            assertThat(SCMRevisionAction.getRevision(source3, a), is(revision3));
        } finally {
            c.close();
        }
    }

    @Test
    public void given__mixedData__when__gettingRevision__then__firstlegacyReturnedForUnmatched() throws Exception {
        MockSCMController c = MockSCMController.create();
        try {
            MockSCMSource source1 = new MockSCMSource("foo", c, "test", true, false, false);
            MockSCMSource source2 = new MockSCMSource("bar", c, "test", true, false, false);
            MockSCMSource source3 = new MockSCMSource("manchu", c, "test", true, false, false);
            SCMRevision revision1 = new MockSCMRevision(new MockSCMHead("head1"), "hash1");
            SCMRevision revision2 = new MockSCMRevision(new MockSCMHead("head2"), "hash2");
            SCMRevision revision3 = new MockSCMRevision(new MockSCMHead("head3"), "hash3");
            Actionable a = new ActionableImpl();
            a.addAction(new SCMRevisionAction(source1, revision1));
            a.addAction(new SCMRevisionAction(revision2, null));
            a.addAction(new SCMRevisionAction(revision3, null));
            assertThat(SCMRevisionAction.getRevision(source1, a), is(revision1));
            assertThat(SCMRevisionAction.getRevision(source2, a), is(revision2));
            assertThat("Cannot distinguish legacy", SCMRevisionAction.getRevision(source3, a), is(revision2));
        } finally {
            c.close();
        }
    }

    private static class ActionableImpl extends Actionable {
        @Override
        public String getDisplayName() {
            return null;
        }

        @Override
        public String getSearchUrl() {
            return null;
        }
    }
}
