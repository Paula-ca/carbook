import { FontAwesomeIcon } from "@fortawesome/react-fontawesome"
import {faInstagram, faLinkedin,faGithubAlt} from "@fortawesome/free-brands-svg-icons"
import React from "react";

const FooterLeft = _ => {
    return(
        <div className="copyright" >
            <img style={{height : '50px'}} src="/logo-carbook-footer.png" alt="logo"/><p></p>
        </div>
    )
}

const SocialMedia = _ => {
    return (
        <div className="footer-right">
            <FontAwesomeIcon className="social-media-icon" icon={faLinkedin} size="2x"/>
            <FontAwesomeIcon className="social-media-icon" icon={faInstagram} size="2x"/>
            <FontAwesomeIcon className="social-media-icon" icon={faGithubAlt} size="2x"/>
        </div>
    )
}

const Footer = _ => {
    return (
        <footer>
            <SocialMedia/>
            <FooterLeft/>
        </footer>
    )
}

export default Footer
