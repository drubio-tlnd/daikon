package org.talend.daikon.spring.reactive.sat.authentication;

import com.nimbusds.jose.shaded.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.talend.daikon.spring.reactive.sat.authentication.TalendJwtConverter.TALEND_ENTITLEMENTS_FIELD;
import static org.talend.daikon.spring.reactive.sat.authentication.TalendJwtConverter.TALEND_PERMISSIONS_FIELD;

class TalendJwtConverterTest {

    private TalendJwtConverter talendJwtConverter;

    @BeforeEach
    void setUp() {
        talendJwtConverter = new TalendJwtConverter();

    }

    @Test
    void convertJwtWithEntittlements() {
        Jwt jwt = Jwt.withTokenValue("fake").header("test", "fake").claim(TALEND_ENTITLEMENTS_FIELD, "perm1,perm2").build();
        Collection<GrantedAuthority> grantedAuthorities = talendJwtConverter.convert(jwt);
        assertNotNull(grantedAuthorities);
        assertEquals(2, grantedAuthorities.size());
    }

    @Test
    void convertJwtWithPermissions() {
        JSONArray permissions = new JSONArray();
        permissions.add("perm1");
        permissions.add("perm2");
        Jwt jwt = Jwt.withTokenValue("fake").header("test", "fake").claim(TALEND_PERMISSIONS_FIELD, permissions).build();
        Collection<GrantedAuthority> grantedAuthorities = talendJwtConverter.convert(jwt);
        assertNotNull(grantedAuthorities);
        assertEquals(2, grantedAuthorities.size());
    }
}
