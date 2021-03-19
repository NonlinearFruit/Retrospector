using System.Windows.Controls;
using Retrospector.Main.Interfaces;

namespace Retrospector.Main
{
    public partial class MainWindow : IMainWindow
    {
        private readonly MainWindowViewModel _viewModel;

        public MainWindow(MainWindowViewModel viewModel)
        {
            InitializeComponent();
            _viewModel = viewModel;
            DataContext = _viewModel;
        }

        private void TextBoxBase_OnTextChanged(object sender, TextChangedEventArgs e)
        {
            var search = searchBox.Text;
            _viewModel.SearchCommand.Execute(search);
        }
    }
}