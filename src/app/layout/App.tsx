import { Container, createTheme, CssBaseline, ThemeProvider } from "@mui/material";
import { useCallback, useEffect, useState } from "react";
import { Route, Switch } from "react-router-dom";
import AboutPage from "../../features/about/AboutPage";
import Header from "./Header";
import ProductDetails from "../../features/catalog/ProductDetails";
import Contact from "../../features/contact/Contact";
import HomePage from "../../features/home/HomePage";
import Catalog from "../../features/catalog/Catalog";
import { ToastContainer } from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';
import NotFound from "../errors/NotFound";
import BasketPage from "../../features/basket/BasketPage";
import LoadingComponent from "./LoadingComponent";
import { useAppDispatch } from "../store/configureStore";
import ServerError from "../errors/ServerError";
import Login from "../../features/account/Login";
import Register from "../../features/account/Register";
import { fetchCurrentUser } from "../../features/account/accountSlice";
import { fetchBasketAsync } from "../../features/basket/basketSlice";
import PrivateRoute from "./PrivateRoute";
import Orders from "../../features/orders/Orders";
import CheckoutWrapper from "../../features/checkout/CheckoutWrapper";
import Inventory from "../../features/admin/Inventory";



function App() {   
  
  const [loading, setLoading] = useState(true);
  const dispatch = useAppDispatch();
  
  const initApp = useCallback(async () => {
    try {
      await dispatch(fetchCurrentUser());
      await dispatch(fetchBasketAsync());
    } catch (error) {
      console.log(error);
    }
  }, [dispatch])
  useEffect(() => {
    initApp().then(() => setLoading(false));
  }, [initApp])

  const [darkMode, setDarkMode] = useState(false);
  const paletteType = darkMode ? 'dark' : 'light'
  const theme = createTheme({
    palette: {
      mode: paletteType,
      background: { 
        default: paletteType === 'light' ? '#eaeaea' : '#121212'
      }
    }
  })

  function handleThemeChange() {
    setDarkMode(!darkMode);
  }

if (loading) return <LoadingComponent message='Ładowanie strony...' />

return (
  <ThemeProvider theme={theme}>
    <ToastContainer position='bottom-right' hideProgressBar theme='colored' />
    <CssBaseline />
    <Header darkMode={darkMode} handleThemeChange={handleThemeChange} />
    <Route exact path='/' component={HomePage} />
    <Route path={'/(.+)'} render={() => (
      <Container  sx={{ mt: 5 }}>
        <Switch>
          <Route exact path='/catalog' component={Catalog} />
          <Route path='/catalog/:id' component={ProductDetails} />
          <Route path='/about' component={AboutPage} />
          <Route path='/contact' component={Contact} />
          <Route path='/server-error' component={ServerError} />
          <Route path='/basket' component={BasketPage} />
          <PrivateRoute path='/checkout' component={CheckoutWrapper} />
          <PrivateRoute path='/orders' component={Orders} />
          <PrivateRoute roles={['Admin']} path='/inventory' component={Inventory} />
          <Route path='/login' component={Login} />
          <Route path='/register' component={Register} />
          <Route component={NotFound} />
        </Switch>
      </Container>
    )} />
    
  </ThemeProvider> 
);
}

export default App;