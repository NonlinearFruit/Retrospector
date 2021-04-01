using System;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using Retrospector.Search.Models;

namespace Retrospector.SearchTab
{
    public partial class SearchTab
    {
        public SearchTab()
        {
            InitializeComponent();
            Loaded += Load;
        }

        private void Load(object sender, RoutedEventArgs e)
        {
            foreach (var attribute in Enum.GetValues<RetrospectorAttribute>())
                searchResultTable.Columns.Add(new DataGridTextColumn
                {
                    Header = attribute.ToString(),
                    Binding = new Binding($"[{(int)attribute}]"),
                    IsReadOnly = true,
                    Width = DataGridLength.SizeToHeader
                });
        }
    }
}