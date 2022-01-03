import { Box, Container, Grid } from "@mui/material";
import { Link } from "react-router-dom";


export default function Footer() {
    return (
        <Box
          px={{ xs: 3, sm: 10 }}
          py={{ xs: 5, sm: 10 }}
          bgcolor="inherit"
          color="white"
        >
          <Container maxWidth="lg">
            <Grid container spacing={5}>
              <Grid item xs={12} sm={4}>
                <Box borderBottom={1}>Pomoc</Box>
                <Box>
                  <Link color="inherit" to={'/contact'}>
                    Kontakt
                  </Link>
                </Box>
                <Box>
                  <Link href="/" color="white" to={""}>
                    Regulamin
                  </Link>
                </Box>
                <Box>
                  <Link href="/" color="inherit" to={""}>
                    Gwarancja
                  </Link>
                </Box>
              </Grid>
              <Grid item xs={12} sm={4}>
                <Box borderBottom={1}>Konto</Box>
                <Box>
                  <Link href="/" color="inherit" to={"/login"}>
                    Logowanie
                  </Link>
                </Box>
                <Box>
                  <Link href="/" color="inherit" to={"/register"}>
                    Rejestracja
                  </Link>
                </Box>
              </Grid>
              <Grid item xs={12} sm={4}>
                <Box borderBottom={1}>Media</Box>
                <Box>
                  <Link href="facebook.com" color="inherit" to={""}>
                    Facebook
                  </Link>
                </Box>
                <Box>
                  <Link href="/" color="inherit" to={""}>
                    Instagram
                  </Link>
                </Box>
                <Box>
                  <Link href="/" color="inherit" to={""}>
                    Twitter
                  </Link>
                </Box>
              </Grid>
            </Grid>
            <Box textAlign="center" pt={{ xs: 5, sm: 10 }} pb={{ xs: 5, sm: 0 }}>
              Sklep &reg; {new Date().getFullYear()}
            </Box>
          </Container>
        </Box>
    )
  }