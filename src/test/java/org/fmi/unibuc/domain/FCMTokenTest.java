package org.fmi.unibuc.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.fmi.unibuc.web.rest.TestUtil;

public class FCMTokenTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FCMToken.class);
        FCMToken fCMToken1 = new FCMToken();
        fCMToken1.setId(1L);
        FCMToken fCMToken2 = new FCMToken();
        fCMToken2.setId(fCMToken1.getId());
        assertThat(fCMToken1).isEqualTo(fCMToken2);
        fCMToken2.setId(2L);
        assertThat(fCMToken1).isNotEqualTo(fCMToken2);
        fCMToken1.setId(null);
        assertThat(fCMToken1).isNotEqualTo(fCMToken2);
    }
}
